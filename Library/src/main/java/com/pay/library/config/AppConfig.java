package com.pay.library.config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wsq on 2016/5/23.
 */
public class AppConfig {

    //    public static final String IP_PORT = "218.66.56.2:28010";//内网测试
    public static final String IP_PORT = "218.66.56.2:19082"; //生产外网
    //   public static  final String IP_PORT ="192.168.211.2:9080";  //生产内网
    public static final String XYIP_PORT = "218.66.56.2:19081"; //生产外网

    public static final String HOST_APP_URL = "http://" + IP_PORT + "/vtmp/App/Api";
    public static final String HOST_YXAPP_URL = "http://" + XYIP_PORT + "/vtmp/App/Api";
    public static final String HOST_SIGN_URL = "http://" + IP_PORT + "/vtmp/Sign/Api";
    public static final String HOST_BR_URL = "http://" + IP_PORT + "/vtmp/BR/Api";
    /**
     * TODO 附件上传路径
     */
    public  static final String uploadDoc_url = "http://218.66.56.2:19081/vtmp/App/Api/uploadDoc";

    /**
     * TODO 登录
     */
    public static final String Login_url = HOST_APP_URL + "/Login";

    /**
     * TODO 注册
     */
    public static final String Register_url = HOST_APP_URL + "/Register";
    /**
     * TODO 人脸识别
     */
    public static final String FaceValidate_url = HOST_APP_URL + "/FaceValidate";

    /**
     * TODO 基础信息补充
     */
    public static final String EssentialInfo_url = HOST_APP_URL + "/SaveBasicInfo";
    /**
     * TODO 工作信息补充
     */
    public static final String WorkInfo_url = HOST_APP_URL + "/SaveWorkInfo";

    /**
     * TODO 紧急联系人
     */
    public static final String Emergency_Contact_url = HOST_APP_URL + "/";

    /**
     * TODO 同步手机通讯录信息
     */
    public static final String syncContact_URL = HOST_APP_URL + "/syncContact";

    /**
     * TODO 银行卡绑定
     */
    public static final String Bank_Card_binding_url = HOST_APP_URL + "/LoanBankValidate";

    /**
     * TODO 小额贷申请
     */
    public static String HOST_LOAN_APPLICation = HOST_APP_URL + "/LoanApplication";

    /***
     *TODO 小额贷状态数据查询
     */
    public static String URL_GET_LOAN__APPLY_STATUS = HOST_APP_URL + "/GetApplyInfo";


    /**
     * TODO 获取贷款记录
     */
    public static final String GetLoanInfoList_url = HOST_APP_URL + "/GetLoanInfoList";

    /**
     * TODO 贷款反欺诈
     */
    public static final String LoanAntiFraud_url = HOST_APP_URL + "/LoanAntiFraud";


    /**
     * TODO 获取短信验证码
     */
    public static final String GetRegSmsCode_url = HOST_APP_URL + "/GetRegSmsCode";
    /**
     * TODO 忘记密码
     */
    public static final String ForgetPwd_url = HOST_APP_URL + "/ForgetPwd";

    /**
     * TODO 修改密码
     */
    public static final String UpdatePwd_url = HOST_APP_URL + "/UpdatePwd";

///////////////////////兴业银行///////////////////////////////////////////
    /**
     * TODO 兴业银行还款帐户变更
     */
    public static final String repayAccChange_url = HOST_YXAPP_URL + "/repayAccChange";

    /**
     * TODO 兴业银行主动还款试算
     */
    public static final String activeRepaymentTry_url = HOST_YXAPP_URL + "/activeRepaymentTry";

    /**
     * TODO 兴业银行主动还款
     */
    public static final String activeRepayment_url = HOST_YXAPP_URL + "/activeRepay";

    /**
     * TODO 兴业银行主动还款状态查询
     */
    public static final String activeRepaStatus_url = HOST_YXAPP_URL + "/activeRepaStatus";

    /**
     * TODO 兴业银行查询欠款
     */
    public static final String queryArrears_url = HOST_YXAPP_URL + "/queryArrears";

    /**
     * TODO 兴业还款记录请求
     */
    public static final String queryRepayList_url = HOST_YXAPP_URL + "/queryRepayList";

    /**
     * TODO 还款计划表
     */
    public static final String GetRepayPlan_url = HOST_YXAPP_URL + "/queryRepayPlan";

    /**
     * TODO 更新下载
     */
    public static final String updateAPP_url = HOST_APP_URL + "/";

    /**
     * TODO 移动支付链接
     */
    public static final String payAPP_url = "http://218.66.56.2:12230/olps/route/index";

    ///////////////////////////////////////////////////////////////////////////////
    /**
     * 1 百融 -- 获取登录元素:1004
     */
    public static final String getLoginResult_url = HOST_BR_URL + "/getLoginResult";
    /**
     * 2 百融 -- 获取手机运营授权验证码
     */
    public static final String sentSms_url = HOST_BR_URL + "/sentSms";

    /**
     * 3 百融 -- 获取手机运营图形授权验证码
     */
    public static final String getImgMsg_url = HOST_BR_URL + "/getImgMsg";

    /**
     * 4 百融 -- 任务数据新建-1000
     */
    public static final String createTask_url = HOST_BR_URL + "/createTask";

    /**
     * 5 百融 -- 爬取手机通话详单 获取运营授权数据-1001
     */
    public static final String queryTask_url = HOST_BR_URL + "/queryTask";

    /**
     * 6 百融 -- 任务数据更新-1002
     */
    public static final String updateTask_url = HOST_BR_URL + "/updateTask";

    /******************************************************************************/


    /**
     * 一、上传图片
     * 1.调用URL
     */
    public static String URL_POST_IMAGE = HOST_APP_URL + "/UploadFile";
    /**
     * 获取签约编号
     */
    public static final String GET_SIGN_NO = HOST_APP_URL + "/UploadContract";
    /**
     * 二、生成签约链接地址
     */
    public static final String CREATE_SIGN_WEB_URL = HOST_SIGN_URL + "/SignLink";

    /**
     * 三、短信签约提醒
     */
    public static final String CREATE_SIGN_SMS_URL = HOST_SIGN_URL + "/SignNotifyRequest";

    /**
     * 签约状态查询
     */
    public static final String SIGNING_STATUS_QUERY_URL = HOST_SIGN_URL + "/SignStatusRequest";

    /**
     * 同步签约状态
     */
    public static final String SYNC_SIGNING_STATE = HOST_APP_URL + "/SyncSignStatus";

    /**
     * 查看签约详情
     */
    public static final String DETAIL_ANONY_LINK = HOST_SIGN_URL + "/DetailAnonyLink";
    /**
     * 六、保全完成合同下载
     */
    public static final String PRES_FILE_LINK = HOST_SIGN_URL + "/PresFileLink";

    /**
     * 五、签约完成合同下载
     */
    public static final String DOWN_SIGN_FILE_LINK = HOST_SIGN_URL + "/DownSignFileLink";

    /***
     *  保存小额贷个人调查基本信息
     */
    public static final String UP_LoanGredit_URL = HOST_APP_URL + "/SyncPerInverstInfo";

    /**
     * 上传小额贷支付成功的订单数据记录
     */
    public static final String UP_LOAN_PAY_ORDER = HOST_APP_URL + "/SyncPayOrderDetail";

    /**
     * 校验小额贷付费是否在有效期内
     */
    public static final String LOAN_PAY_ORDER_AUTH_Term_of_validity = HOST_APP_URL + "/CheckOrder";


    /**
     * 主线路径生产环境
     */
    public static String HOST = "http://112.5.192.197:8098/mpcctp/";

    /****小额贷二维码获取****/
    public static String getQrURL = "http://218.66.56.2:19081/vtmp/posm/app/getQR";//正式

    /*** 小额贷二维码支付情况查询**/
    public static String queryQRPayURL = "http://218.66.56.2:19081/vtmp/posm/app/queryQRPay";//正式
    /*********************************************/

    public static final String DEFAULT_TERMNAL_NO = "999999999";

    /**
     * 服务协议
     */
    public static String GET_AGREEMENT = "http://112.5.192.197:8899/pay/test/agreement.html";
    /**
     * 分享路径
     */
    public static String SHARE_URL = "http://112.5.192.197:8899/pay/auth/appLoad.do";

    //
    public static final boolean DEBUG = true;

    /**
     * 设置二维码交易检查的轮询时间
     */
    public static final int QR_CODE_PAY_TIME = 5 * 1000;
    /**
     * 设置扫码交易时的延迟时间
     */
    public static final int QR_CODE_DELAY_TIME = 8 * 1000;


    /***
     * 支付类型
     * @author wsq
     *
     */
    public static class PAYTYPE {

        static Map<String, String> map = new HashMap<String, String>();
        /**
         * 支付账户
         */
        public static final String PAY_ACCOUNT = "01";

        /**
         * 终端
         */
        public static final String TERM = "02";

        /**
         * 快捷支付
         */
        public static final String QUICK = "03";

        /**
         * 扫码支付
         */
        public static final String QR_CODE = "04";


        static {
            map.put(PAY_ACCOUNT, "支付账户");
            map.put(TERM, "终端");
            map.put(QUICK, "快捷支付");
            map.put(QR_CODE, "扫码支付");
        }

        public static String getValue(String key) {

            return map.get(key) == null ? "" : map.get(key);
        }

    }


    /***
     *提现方式
     *
     */
    public static class ACCTTYPE {

        static Map<String, String> map = new HashMap<String, String>();
        /**
         * 支付账户
         */
        public static final String PAY_ACCOUNT = "01";

        /**
         * 终端
         */
        public static final String TERM = "02";

        /**
         * 快捷支付
         */
        public static final String QUICK = "03";

        /**
         * 扫码支付
         */
        public static final String QR_CODE = "04";


        static {
            map.put(PAY_ACCOUNT, "所有账户");
            map.put(TERM, "普通收单账户");
            map.put(QUICK, "快捷交易账户");
            map.put(QR_CODE, "扫码支付");

        }

        public static String getValue(String key) {

            return map.get(key) == null ? "" : map.get(key);
        }

    }

    /**
     * 提现类型
     *
     * @author wsq
     */
    public static class CASTYPE {


        /**
         * T0提现
         */
        public static final String CAS_TYPE_T0 = "1";

        /**
         * T1提现
         */
        public static final String CAS_TYPE_T2 = "2";

        /**
         * 3:不区分混合式提现
         */
        public static final String CAS_TYPE_UN = "3";


    }

    /**
     * 业务类型
     *
     * @author wsq
     */
    public static class BUSTYPE {
        static Map<String, String> map = new HashMap<String, String>();
        /**
         * 所有
         */
        public static final String BUS_TYPE_00 = "00";

        /**
         * 收款
         */
        public static final String BUS_TYPE_01 = "01";

        /**
         * 消费
         */
        public static final String BUS_TYPE_02 = "02";
        /**
         * 提现
         */
        public static final String BUS_TYPE_03 = "03";

        static {
            map.put(BUS_TYPE_00, "所有");
            map.put(BUS_TYPE_01, "收款");
            map.put(BUS_TYPE_02, "消费");
            map.put(BUS_TYPE_03, "提现");
        }

        public static String getValue(String key) {
            return map.get(key) == null ? "" : map.get(key);
        }

    }

    /**
     * 订单状态
     *
     * @author wsq
     */

    public static class ORDSTATUS {
        static Map<String, String> map = new HashMap<String, String>();

        /**
         * 未交易
         */
        public static final String ORD_STATUS_00 = "00";

        /**
         * 成功
         */
        public static final String ORD_STATUS_01 = "01";

        /**
         * 失败
         */
        public static final String ORD_STATUS_02 = "02";

        /**
         * 可疑
         */
        public static final String ORD_STATUS_03 = "03";

        /**
         * 处理中
         */
        public static final String ORD_STATUS_04 = "04";

        /**
         * 已取消
         */
        public static final String ORD_STATUS_05 = "05";


        /**
         * 未支付
         */
        public static final String ORD_STATUS_06 = "06";

        /**
         * 已退货
         */
        public static final String ORD_STATUS_07 = "07";

        /**
         * 退货中
         */
        public static final String ORD_STATUS_08 = "08";

        /**
         * 部分退货
         */
        public static final String ORD_STATUS_09 = "09";


        static {

            map.put(ORD_STATUS_00, "未交易");
            map.put(ORD_STATUS_01, "成功");
            map.put(ORD_STATUS_02, "失败");
            map.put(ORD_STATUS_03, "可疑");
            map.put(ORD_STATUS_04, "处理中");
            map.put(ORD_STATUS_05, "已取消");
            map.put(ORD_STATUS_06, "未支付");
            map.put(ORD_STATUS_07, "已退货");
            map.put(ORD_STATUS_08, "退货中");
            map.put(ORD_STATUS_09, "部分退货");

        }

        public static String getContent(String key) {

            return map.get(key) == null ? "" : map.get(key);
        }

    }

    /**
     * 二维码扫描的类型
     *
     * @author wsq
     */
    public static class SCANTYPE {

        static Map<String, String> map = new HashMap<String, String>();

        /**
         * 微信支付
         */
        public static final String SCAN_TYPE_00 = "00";

        static {
            map.put(SCAN_TYPE_00, "微信");
        }

        public static String getScanType(String key) {

            return map.get(key) == null ? "" : map.get(key);
        }
    }

    public static Map<String, String> mtdcdeMap = new HashMap<>();

    static {
        mtdcdeMap.put("0", "一次性还本付息");
        mtdcdeMap.put("2", "按月等额本息");
    }

    public static Map<String, String> applyStatusMap = new HashMap<>();

    static {
        applyStatusMap.put("006", "复审");
        applyStatusMap.put("101", "复审拒绝");
        applyStatusMap.put("200", "终审");
        applyStatusMap.put("201", "终审拒绝");
        applyStatusMap.put("300", "签约");
        applyStatusMap.put("301", "签约拒绝");
        applyStatusMap.put("302", "复查");
        applyStatusMap.put("303", "签约中");
        applyStatusMap.put("401", "发放申请");
        applyStatusMap.put("403", "发放确认");
        applyStatusMap.put("400", "已放款");
        applyStatusMap.put("402", "发放拒绝");
        applyStatusMap.put("500", "完成");
    }

    /**
     * 新业进件流程状态
     */
    public static Map<String, String> applyXYStatusMap = new HashMap<>();

    static {
        applyXYStatusMap.put("ACCEPT", "进件接收成功");
        applyXYStatusMap.put("PROCESSING", "进件处理中");
        applyXYStatusMap.put("REJECT", "进件拒绝");
        applyXYStatusMap.put("APPROVAL_PROCESSING", "审批处理中");
        applyXYStatusMap.put("CC_LOAN_PROCESSING", "核心进件处理中");
        applyXYStatusMap.put("LOAN_REJECT", "贷款被拒绝");
        applyXYStatusMap.put("LC_PATCH", "补件");
        applyXYStatusMap.put("APPROVAL_PASS", "审批通过，等待放款");
        applyXYStatusMap.put("LENDING_PROCESSING", "放款处理中");
        applyXYStatusMap.put("HAS_BEEN_LENDING", "已放款");
        applyXYStatusMap.put("LENDING_FAIL", "放款失败");
    }

    /**
     * VTM产品类型
     */
    public static Map<String, String> applyProductType = new HashMap<>();

    static {
        applyProductType.put("1001", "消费分期贷");
        applyProductType.put("1002", "工薪贷");
        applyProductType.put("1003", "精英贷");
        applyProductType.put("1004", "业主贷");
        applyProductType.put("1005", "车抵贷");
        applyProductType.put("1006", "房抵贷");
        applyProductType.put("0101", "受薪客户专案");
        applyProductType.put("0102", "个私业主贷");
        applyProductType.put("0103", "房贷专案贷");
        applyProductType.put("0104", "房产专案贷");
        applyProductType.put("0105", "车贷专案贷");
        applyProductType.put("0106", "车产专案贷");
        applyProductType.put("0107", "行内房产抵押");
        applyProductType.put("0108", "低风险客户专案");
        applyProductType.put("0109", "消费贷(信用保证类)");
        applyProductType.put("0115", "兴业银行房贷客户交叉营销专案");
        applyProductType.put("0116", "兴业银行信用卡客户交叉营销专案");
        applyProductType.put("0117", "上海抵押房产客户专案");
        applyProductType.put("0118", "集团内金融资产客户交叉营销专案");

    }

    public static class signStatus {
        public static Map<String, String> statusMap = new HashMap<>();

        static {
            statusMap.put("1", "已签");
            statusMap.put("2", "拒签");
            statusMap.put("3", "保全");
        }

    }

    public static class Channel {
        public static final String huiAn = "10010";
        public static final String qinLiu = "10011";
        public static final String changZhi = "10020";
        public static final String ziYouZijing = "10030";
        public static final String xingYe = "10040";
    }

}
