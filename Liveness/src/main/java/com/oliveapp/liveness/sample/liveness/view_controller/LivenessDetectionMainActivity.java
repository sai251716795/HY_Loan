package com.oliveapp.liveness.sample.liveness.view_controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oliveapp.camerasdk.PhotoModule;
import com.oliveapp.camerasdk.utils.CameraUtil;
import com.oliveapp.face.livenessdetectionviewsdk.event_interface.ViewUpdateEventHandlerIf;
import com.oliveapp.face.livenessdetectionviewsdk.uicomponents.CircleImageView;
import com.oliveapp.face.livenessdetectionviewsdk.uicomponents.CircularCountDownProgressBar;
import com.oliveapp.face.livenessdetectionviewsdk.utils.AudioModule;
import com.oliveapp.face.livenessdetectionviewsdk.verification_controller.VerificationController;
import com.oliveapp.face.livenessdetectorsdk.datatype.AccessInfo;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.configs.ApplicationParameters;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.FacialActionType;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.ImageProcessParameter;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectionFrames;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessDetectorConfig;
import com.oliveapp.face.livenessdetectorsdk.livenessdetector.datatype.LivenessSessionState;
import com.oliveapp.face.livenessdetectorsdk.prestartvalidator.datatype.PrestartDetectionFrame;
import com.oliveapp.face.livenessdetectorsdk.utilities.utils.LogUtil;
import com.oliveapp.face.livenessdetectorsdk.utilities.utils.PackageNameManager;
import com.oliveapp.liveness.R;

import junit.framework.Assert;

/**
 * ViewController 实现了主要的界面逻辑
 * 如果需要定义界面，请继承此类编写自己的Activity，并自己实现事件响应函数
 * 可参考SampleAPP里的ExampleLivenessActivity
 */
public abstract class LivenessDetectionMainActivity extends Activity implements ViewUpdateEventHandlerIf {
    public static final int INSTANT_CHANGE = 1;
    public static final int FLUENT_CHANGE = 2;
    public static final String TAG = LivenessDetectionMainActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 10001;

    // Camera Preview Parameter
    private static final float TARGET_PREVIEW_RATIO = 4 / 3f; // 摄像头Previwe预览界面的长宽比 默认使用4:3
    private static final int MAX_PREVIEW_WIDTH = 961; // 摄像头Preview预览界面的最大宽度，默认使用分辨率1280x960是可以平衡预览质量和处理速度的
    // Animation
    private static final int ANIMATION_INTERVAL_MILLISECOND = 1000;

    private String mPackageName; // 包名
    private PhotoModule mPhotoModule; // 摄像头模块
    private AudioModule mAudioModule;
    private int mActionChangeMode; // INSTANT_CHANGE, FLUENT_CHANGE

    /// 动作提示相关的Layout和View
    private RelativeLayout mOliveappStepHintLayout; // 动作提示Layout
    private CircleImageView mOliveappStepHintImage; // 动作提示动画
    private TextView mOliveappStepHintText; // 动作提示文字
    private CircularCountDownProgressBar mOliveappCountDownProgressbar; // 动作提示倒计时

    /// 显示结果的View
    private RelativeLayout mOliveappResultLayout; // 显示结果的View
    private ImageView mOliveappResultIcon; // 结果提示文字
    private TextView mOliveappResultText; // 结果提示文字

    boolean mIsLivenessFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!PackageNameManager.isPackageNameSet()) {
            PackageNameManager.setPackageName(getPackageName());
        }
        com.oliveapp.camerasdk.utils.PackageNameManager.setPackageName(PackageNameManager.getPackageName());
        increaseClassObjectCount();

        Log.i(TAG, "[BEGIN] LivenessDetectionMainActivity::onCreate()");
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 拍照权限
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        //这里设置动作切换模式
        //INSTANT_CHANGE,活体检测动作成功后不等语音播放完就进入下一个动作
        //FLUENT_CHANGE,活体检测动作成功后要等语音播放完才进入下一个动作
        mActionChangeMode = FLUENT_CHANGE;
        mPackageName = PackageNameManager.getPackageName();

        // 初始化界面元素
        initViews();
        // 初始化摄像头
        initCamera();
        // 初始化检测逻辑控制器(VerificationController)
        initControllers();


        Log.i(TAG, "[END] LivenessDetectionMainActivity::onCreate()");
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "[BEGIN] LivenessDetectionMainActivity::onResume()");
        super.onResume();

        if (mPhotoModule != null) {
            mPhotoModule.onResume();
            // 设置摄像头回调，自此之后VerificationController.onPreviewFrame函数就会源源不断的收到摄像头的数据
        } else {
            // 如果按了HOME键或中间被打断，直接判定为CANCALLED
            onLivenessFail(LivenessSessionState.SESSION_CANCELLED, null);
        }

        try {
            mPhotoModule.setPreviewDataCallback(mVerificationController, mCameraHandler);
        } catch (NullPointerException e) {
            Log.e(TAG, "PhotoModule set callback failed", e);
        }

        if (mAudioModule != null) {
            mAudioModule.playAudio(this, "oliveapp_step_hint_getready");
        }
        Log.i(TAG, "[END] LivenessDetectionMainActivity::onResume()");
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "[BEGIN] LivenessDetectionMainActivity::onPause()");
        super.onPause();

        if (mPhotoModule != null)
            mPhotoModule.onPause();
        Log.i(TAG, "[END] LivenessDetectionMainActivity::onPause()");
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "[BEGIN] LivenessDetectionMainActivity::onStop()");
        super.onStop();

        // 关闭摄像头
        if (mPhotoModule != null)
            mPhotoModule.onStop();
        CameraUtil.sContext = null;
        mPhotoModule = null;

        if (mAudioModule != null) {
            mAudioModule.release();
            mAudioModule = null;
        }

        if (mOliveappStepHintImage != null)
            mOliveappStepHintImage.stop();
        mOliveappStepHintImage = null;

        // 退出摄像头处理线程
        if (mCameraHandlerThread != null) {
            try {
                mCameraHandlerThread.quit();
                mCameraHandlerThread.join();
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "Fail to join CameraHandlerThread", e);
            }
        }
        mCameraHandlerThread = null;

        // 销毁检测逻辑控制器
        if (mVerificationController != null)
            mVerificationController.uninit();
        mVerificationController = null;

        // 销毁倒计时View
        if (mOliveappCountDownProgressbar != null)
            mOliveappCountDownProgressbar.destory();
        mOliveappCountDownProgressbar = null;

        Log.i(TAG, "[END] LivenessDetectionMainActivity::onStop()");
    }

    //////////////////////////// INIT ////////////////////////////////

    private VerificationController mVerificationController; // 逻辑控制器
    private Handler mCameraHandler; // 摄像头回调所在的消息队列
    private HandlerThread mCameraHandlerThread; // 摄像头回调所在的消息队列线程

    /**
     * 初始化并打开摄像头
     */
    private void initCamera() {
        LogUtil.i(TAG, "[BEGIN] initCamera");

        // 寻找设备上的前置摄像头
        int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        int expectCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT;
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);

            LogUtil.i(TAG, "camera id: " + camIdx + ", facing: " + cameraInfo.facing + ", expect facing: " + expectCameraFacing);
            if (cameraInfo.facing == expectCameraFacing) {
                getIntent().putExtra(CameraUtil.EXTRAS_CAMERA_FACING, camIdx); // 设置需要打开的摄像头ID
                getIntent().putExtra(CameraUtil.MAX_PREVIEW_WIDTH, MAX_PREVIEW_WIDTH); // 设置最大Preview宽度
                getIntent().putExtra(CameraUtil.TARGET_PREVIEW_RATIO, TARGET_PREVIEW_RATIO); // 设置Preview长宽比
            }
        }
        mPhotoModule = new PhotoModule();
//        mPhotoModule.init(this, findViewById(getResources().getIdentifier("oliveapp_cameraPreviewView", "id", mPackageName))); // 参考layout XML文件里定义的cameraPreviewView对象
        mPhotoModule.init(this, findViewById(R.id.oliveapp_cameraPreviewView)); // 参考layout XML文件里定义的cameraPreviewView对象
        mPhotoModule.setPlaneMode(false, false); // 取消拍照和对焦功能
        // 打开摄像头预览
        mPhotoModule.onStart();

        // 初始化摄像头处理消息队列
        mCameraHandlerThread = new HandlerThread("CameraHandlerThread");
        mCameraHandlerThread.start();
        mCameraHandler = new Handler(mCameraHandlerThread.getLooper());

        LogUtil.i(TAG, "[END] initCamera");
    }

    /**
     * 初始化界面元素
     */
    private void initViews() {
        // Fullscreen looks better
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            /*View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);*/

            // Style中已经设置了NoActionBar
//            ActionBar actionBar = getActionBar();
//            actionBar.hide();
        }

        setContentView(getResources().getIdentifier("oliveapp_activity_liveness_detection_main", "layout", mPackageName));

        /// 步骤提示界面相关
        mOliveappStepHintLayout = (RelativeLayout) findViewById(getResources().getIdentifier("oliveapp_step_hint_layout", "id", mPackageName));
        mOliveappStepHintImage = (CircleImageView) findViewById(getResources().getIdentifier("oliveapp_step_hint_image", "id", mPackageName));
        mOliveappStepHintImage.start();
        mOliveappStepHintText = (TextView) findViewById(getResources().getIdentifier("oliveapp_step_hint_text", "id", mPackageName));
        mOliveappCountDownProgressbar = (CircularCountDownProgressBar) findViewById(getResources().getIdentifier("oliveapp_step_countdown_progressbar", "id", mPackageName));
        mOliveappCountDownProgressbar.setVisibility(View.INVISIBLE);

        /// 结果展示界面相关
        mOliveappResultLayout = (RelativeLayout) findViewById(getResources().getIdentifier("oliveapp_step_hint_layout", "id", mPackageName));
        mOliveappResultIcon = (ImageView) findViewById(getResources().getIdentifier("oliveapp_result_icon", "id", mPackageName));
        mOliveappResultText = (TextView) findViewById(getResources().getIdentifier("oliveapp_result_text", "id", mPackageName));

        mAudioModule = new AudioModule();

        // DEBUG: 调试相关
        mFrameRateText = (TextView) findViewById(getResources().getIdentifier("oliveapp_frame_rate_text", "id", mPackageName));
    }

    // 图片预处理参数
    private ImageProcessParameter mImageProcessParameter;
    // 活体检测参数
    private LivenessDetectorConfig mLivenessDetectorConfig;

    /**
     * 设置图片处理参数和活体检测参数
     */
    private void setDetectionParameter() throws Exception {
        /**
         * 注意: 默认参数适合手机，一般情况下不需要修改这些参数。如需修改请联系百融工程师
         *
         * 设置从preview图片中截取人脸框的位置，调用doDetection前必须调用本函数。
         * @param preRotationDegree　逆时针旋转角度，只允许0 90 180 270，大部分手机应当是90
         *                         以下说明中的帧都是指旋转后的帧 旋转之后的帧建议宽高比例3:4，否则算法结果无法保证
         * @param cropWidthPercent　截取的人脸框宽度占帧宽度的比例
         * @param verticalOffsetPercent　截取的人脸框上边缘到帧上边缘的距离占帧高度的比例，算法真正截取的人脸的尺寸=cropWidthPercent*4/3，再依据这个参数获取需要截取的位置
         * @param shouldFlip 是否左右翻转。一般前置摄像头为false
         */
        mImageProcessParameter = new ImageProcessParameter(false, 1.0f, 0.0f, 90);

        /**
         * 动作生成规则和通过规则配置
         * 注意: 默认参数平衡了通过率和防Hack能力，如需修改配置建议咨询百融工程师
         * SDK提供了三种配置方式
         */

        // 使用预设配置: 满足绝大多数常见场景
        mLivenessDetectorConfig = new LivenessDetectorConfig();
        // 默认配置为: 3个动作，不允许动作失败，10s超时
        mLivenessDetectorConfig.usePredefinedConfig(0);
        /*
        // 部分自定义配置: 可以修改全部或部分参数
        // 举例：如果只需要修改超时时间，可以设置timeoutMs变量
        livenessDetectorConfig.totalActions = 3; // 3个动作
        livenessDetectorConfig.minPass = 3; // 最少通过3个动作
        livenessDetectorConfig.maxFail = 0; // 1个动作都不允许失败
        livenessDetectorConfig.timeoutMs = 10000; // 单个动作超时时间为10s
        livenessDetectorConfig.fixedActions = false; // 不固定动作
        // 候选动作为抬头、左右摇头、张嘴、闭眼
        livenessDetectorConfig.candidateActionList = Arrays.asList(FacialActionType.HEAD_UP, FacialActionType.HEAD_SHAKE_SIDE_TO_SIDE, FacialActionType.MOUTH_OPEN, FacialActionType.EYE_CLOSE);
        */

        /*
        // 完全自定义动作: 修改时请和百融工程师确认
        livenessDetectorConfig.totalActions = 3;
        livenessDetectorConfig.minPass = 3;
        livenessDetectorConfig.maxFail = 0;
        livenessDetectorConfig.timeoutMs = 10000;
        livenessDetectorConfig.fixedActions = true;
        livenessDetectorConfig.fixedActionList = Arrays.asList(FacialActionType.HEAD_SHAKE_SIDE_TO_SIDE, FacialActionType.MOUTH_OPEN, FacialActionType.EYE_CLOSE);
        */
        // 校验配置是否正确 (出错会抛异常，异常的Message里有中文提示原因)
        if (mLivenessDetectorConfig != null)
            mLivenessDetectorConfig.validate();
    }

    /**
     * 初始化检测逻辑控制器
     * 请先调用setDetectionParameter()设置参数
     */
    private void initControllers() {
        try {
            setDetectionParameter();
        } catch (Exception e) {
            LogUtil.e(TAG, "Failed to set parameter...", e);
        }

        // init verification controller
        mVerificationController = new VerificationController(AccessInfo.getInstance(),
                LivenessDetectionMainActivity.this, mImageProcessParameter, mLivenessDetectorConfig,
                LivenessDetectionMainActivity.this, new Handler(Looper.getMainLooper()), mActionChangeMode);
    }

    //////////////// INTERFACE 对外接口 /////////////////
    //////////////// 可以在子类中调用    /////////////////

    /**
     * 调用此函数后活体检测即开始
     * 可以用这个函数来实现类似 用户点击“开始”按钮，倒计时3秒后才启动的功能
     * 如需设置参数，请先调用setDetectionParameter()
     */
    public void startVerification() {
        try {
            if (mVerificationController.getCurrentStep() == VerificationController.STEP_READY) {
                mVerificationController.nextVerificationStep();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "无法开始活体检测...", e);
        }
    }


    ////////////// INITIALIZATION //////////////
    @Override
    public void onInitializeSucc() {
        // 初始化成功
    }

    @Override
    public void onInitializeFail(Throwable e) {
        // 初始化失败
    }

    ////////////// LIVENESS DETECTION 活体检测 /////////////////

    /**
     * 这里只处理了一部分回调，活体检测成功和失败的回调由APP实现
     * 参考SampleAPP的ExampleLivenessActivity
     */

    @Override
    public void onActionChanged(int lastActionType, int lastActionResult, int newActionType, int currentActionIndex) {
        /**
         * [活体检测阶段]
         * 活体检测动作切换时的回调函数
         */
        LogUtil.i(TAG, "[BEGIN] onActionChanged, current action index: " + currentActionIndex + " , " + lastActionType + " -> " + newActionType + ", result: " + lastActionResult);
        try {
            final int fnewActionType = newActionType;
            final int flastActionResult = lastActionResult;

            if (mActionChangeMode == FLUENT_CHANGE) {
                //先播放“很好”，然后播放下一个动作提示音
                mOliveappCountDownProgressbar.setVisibility(View.INVISIBLE);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (mAudioModule != null && mAudioModule.isPlaying()) {
                                //上一段还没播放完
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //播放“很好”,更改提示问题
                            if (flastActionResult == 1000) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mOliveappStepHintText.setText("很好");
                                    }
                                });
                                mAudioModule.playAudio(LivenessDetectionMainActivity.this, "oliveapp_step_hint_nextaction");
                            }
                            while (mAudioModule != null && mAudioModule.isPlaying()) {
                                //等“很好播放完后再进入下一个动作
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    changeToNextAction(fnewActionType);
                                }
                            });
                        } catch (Exception e) {
                            LogUtil.i(TAG, "Thread interrupt");
                        }
                    }
                });
                t.start();
            } else {
                changeToNextAction(fnewActionType);
            }
            LogUtil.i(TAG, "[END] onActionChanged");
        } catch (Exception e) {
            LogUtil.i(TAG, "onActionChanged interrupt");
        }
    }

    /**
     * 切换到下一个动作
     */
    public void changeToNextAction(int newActionType) {

        try {
            // 更新提示文字
            String hintText = getString(getResources().getIdentifier("oliveapp_step_hint_normal", "string", mPackageName));
            switch (newActionType) {
                case FacialActionType.UNKNOWN:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_normal", "string", mPackageName));
                    break;
                case FacialActionType.MOUTH_OPEN:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_mouthopen", "string", mPackageName));
                    break;
                case FacialActionType.EYE_CLOSE:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_eyeclose", "string", mPackageName));
                    break;
                case FacialActionType.HEAD_LEFT:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_headleft", "string", mPackageName));
                    break;
                case FacialActionType.HEAD_RIGHT:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_headright", "string", mPackageName));
                    break;
                case FacialActionType.HEAD_UP:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_headup", "string", mPackageName));
                    break;
                case FacialActionType.HEAD_SHAKE_SIDE_TO_SIDE:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_headshake", "string", mPackageName));
                    break;
                default:
                    hintText = getString(getResources().getIdentifier("oliveapp_step_hint_normal", "string", mPackageName));
            }

            // 更新提示动画
            mOliveappStepHintImage.updateAnimation(FacialActionType.getStepHintAnimationList(newActionType), ANIMATION_INTERVAL_MILLISECOND);
            mAudioModule.playAudio(LivenessDetectionMainActivity.this, FacialActionType.getStringResourceName(newActionType));
            mOliveappStepHintText.setText(hintText);
            mVerificationController.nextAction();
            mOliveappCountDownProgressbar.setRemainTimeSecond((int) ApplicationParameters.ACTION_TIMEOUT_MILLISECOND, (int) ApplicationParameters.ACTION_TIMEOUT_MILLISECOND);
            mOliveappCountDownProgressbar.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtil.i(TAG, "changeToNextAction interrupt");
        }
    }

    @Override
    public void onFrameDetected(int currentActionType, int actionState, int sessionState, int remainingTimeoutMilliSecond) {
        /**
         * [活体检测阶段]
         * 活体检测每帧处理结束后的回调函数
         */
        LogUtil.i(TAG, "[BEGIN] onFrameDetected");
        LogUtil.i(TAG, "[BEGIN] onFrameDetected " + remainingTimeoutMilliSecond);

        mOliveappCountDownProgressbar.setRemainTimeSecond(remainingTimeoutMilliSecond, (int) ApplicationParameters.ACTION_TIMEOUT_MILLISECOND);

        mFrameRate += 1;
        long currentTimestamp = System.currentTimeMillis();
        if ((currentTimestamp - mLastTimestamp) > 1000) {
            mLastTimestamp = currentTimestamp;
            mFrameRateText.setText("FrameRate: " + mFrameRate + " FPS");
            mFrameRate = 0;
        }

        LogUtil.i(TAG, "[END] onFrameDetected");
    }

    @Override
    public void onLivenessSuccess(LivenessDetectionFrames livenessDetectionFrames) {
        try {
            mAudioModule.playAudio(LivenessDetectionMainActivity.this, "oliveapp_step_hint_verificationpass");
        } catch (Exception e) {
            LogUtil.e(TAG, "TODO", e);
        }
    }

    @Override
    public void onLivenessFail(int result, LivenessDetectionFrames livenessDetectionFrames) {
        Log.e(TAG, "onLivenessFail: /************************************************/" );
        Log.e(TAG, "onLivenessFail: result:"+result);
        Log.e(TAG, "onLivenessFail: /************************************************/" );

        try {
            if (LivenessSessionState.SESSION_FAIL == result)
                mAudioModule.playAudio(LivenessDetectionMainActivity.this, "oliveapp_step_hint_verificationfail");
            else if (LivenessSessionState.SESSION_TIMEOUT == result)
                mAudioModule.playAudio(LivenessDetectionMainActivity.this, "oliveapp_step_hint_timeout");
        } catch (Exception e) {
            LogUtil.e(TAG, "522", e);
        }
    }

    ////////////// PRESTART 预检步骤 /////////////////

    @Override
    public void onPrestartFrameDetected(PrestartDetectionFrame frame, int remainingTimeMillisecond) {
        /**
         * [预检阶段]
         * 每帧处理完成的回调函数
         * 当前流程设计下不需要处理此事件
         */
        LogUtil.i(TAG, "[BEGIN] onPrestartFrameDetected");
        LogUtil.i(TAG, "[END] onPrestartFrameDetected");
    }


    @Override
    public void onPrestartSuccess(LivenessDetectionFrames livenessDetectionFrames) {
        /**
         * [预检阶段]
         * 检测到符合质量的人脸，认为可以开始活体检测的回调
         */
        LogUtil.i(TAG, "[BEGIN] onPrestartSuccess");
        if (FLUENT_CHANGE == mActionChangeMode) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mAudioModule != null && mAudioModule.isPlaying()) {
                        //等待播放完毕
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mVerificationController.enterLivenessDetection();
                }
            });
            thread.start();
        } else {
            mVerificationController.enterLivenessDetection();
        }
        LogUtil.i(TAG, "[END] onPrestartSuccess");
    }

    @Override
    public void onPrestartFail(int result) {
        /**
         * [预检阶段]
         * 无法检测到符合质量的人像的事件回调
         * 当前流程设计下不会触发此函数
         * 可以用这个函数接收启动检测超时的事件
         */
        LogUtil.wtf(TAG, "[END] onPrestartFail");
    }

    /////////////////// FOR DEBUG //////////////////////
    private TextView mFrameRateText;
    private long mLastTimestamp = System.currentTimeMillis();
    private int mFrameRate = 0;
    private static int classObjectCount = 0;

    private void increaseClassObjectCount() {
        // DEBUG: 检测是否有Activity泄漏
        classObjectCount++;
        Log.i(TAG, "LivenessDetectionMainActivity classObjectCount onCreate: " + classObjectCount);

        // 预期现象是classObjectCount会在1~2之间抖动，如果classObjectCount一直在增长，很可能有内存泄漏
        if (classObjectCount == 10) {
            System.gc();
        }
        Assert.assertTrue(classObjectCount < 10);
    }

    @Override
    public void finalize() {
        try {
            super.finalize();
        } catch (Throwable e) {
            LogUtil.e(TAG, "无法完成finalize...", e);
        }
        // DEBUG: 检测是否有Activity泄漏。与increaseClassObjectCount对应
        classObjectCount--;
        Log.i(TAG, "LivenessDetectionMainActivity classObjectCount finalize: " + classObjectCount);
    }

}
