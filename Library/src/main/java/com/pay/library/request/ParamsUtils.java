package com.pay.library.request;

/**
 * Created by wsq on 2016/5/23.
 */
public class ParamsUtils {


    /*************************************请求参数************************************************/

    public static final String  CUST_MOBILE           = "custMobile";               //用户名/手机号码

    public static final String  CUST_PWD              = "custPwd";                  //密码

    public static final String  CODE_TYPE             = "codeType";                 //验证码类型  01，注册 02. 登录密码修改或找回  03，支付密码修改或找回

    public static final String  MSG_CODE              = "msgCode";                  //验证码

    public static final String  SYS_TYPE              = "sysType";                  //客户端类型   1 Android 2 ios

    public static final String SYS_VERSION            = "sysVersion";               // 设备系统版本号

    public static final String APP_NAME               = "appName";                  //APP版本名称

    public static final String SYS_TER_NO             = "sysTerNo";                 //可以用来确定一台设备的固定设备号

    public static final String CUST_ID                = "custId";                   //商户id

    public static final String TXN_DATE               = "txnDate";                  //交易日期

    public static final String TXN_TIME               = "txnTime";                  //交易时间

    public static final String TOKEN                  = "token";

    public static final String PWD_TYPE               = "pwdType";                  //密码类型：1-登录密码；2-支付密码

    public static final String UPDATE_TYPE            = "updateType";               //1：根据原密码修改；2：根据短信验证码找回

    public static final String VALUE                  = "value";                    //短信验证码或原始密码

    public static final String NEW_PSD                = "newPwd";                   //新密码

    public static final String CARD_HAND_HELD         = "cardHandheld";             //手持身份证照片

    public static final String CARD_FRONT             = "cardFront";                //身份证正面照片

    public static final String CARD_BACK              = "cardBack";                 //身份证背面照片

    public static final String CUST_NAME              = "custName";                 //注册商户身份证上对应名称

    public static final String CERTIFICATE_TYPE       = "certificateType";          //证件类型   01,身份证  02，护照  03，军官证

    public static final String CERTIFICATE_NO         = "certificateNo";            //证件号码

    public static final String USER_EMAIL             = "userEmail";                //邮箱

    public static final String PROVINCE_ID            = "provinceId";               //省份id

    public static final String CITY_ID                = "cityId";                   //城市ID

    public static final String PAY_PWD                = "payPwd";                   //交易密码

    public static final String OPER_TYPE              = "operType";                 //操作类型  1-银行卡绑定；2-银行卡信息修改；3-设为默认银行卡，4-解绑(删除)

    public static final String OLD_CARD_NO            = "oldCardNo";                //原银行卡号

    public static final String CARD_NO                = "cardNo";                   //银行卡号
    
    public static final String CARD_CVV 			  = "cardCvv";					//卡叫验值   卡背面后3位数字
    
    public static final String CARD_EXPIRE_DATE 	  = "cardExpireDate";			//有效期限  格式YYMM
    
    public static final String MOBILE_NO 			  = "mobileNo";					//手机号码  开户行预留手机号

    public static final String CNAPS_CODE             = "cnapsCode";                //联行号

    public static final String SUB_BRANCH             = "subBranch";                //支行名称

    public static final String START                  = "start";                    //开始的行数

    public static final String PAGE_SIZE              = "pageSize";                 //每次刷新的条数

    public static final String NOTICE_STATUS          = "noticeStatus";             //区别公告查询

    public static final String LAST_ID                = "lastId";                   //上次加载完成后最后一条公告的Id

    public static final String ID                     = "Id";                       //

    public static final String NOTICE_ID              = "noticeId";                 //公告ID

    public static final String BANK_PRO_ID            = "bankProId";                //省ID

    public static final String BANK_CITY_ID           = "bankCityId";

    public static final String BANK_NAME              = "bankName";                 //银行名称

    public static final String CATEGORIES             = "categories";

    public static final String TERM_NO                = "termNo";                   //终端号

    public static final String TERM_TYPE              = "termType";                 //01 蓝牙 02 音频

    public static final String PRDORD_TYPE            = "prdordType";               //订单类型  01 收款

    public static final String PRDORD_AMT             = "prdordAmt";                //订单金额

    public static final String PAY_TYPE               = "payType";                  //支付方式  01支付账户  02终端  03快捷
    public static final String PAY_TYPE_U             = "PAYTYPE";

    public static final String RATE_TYPE              = "rateType";                 //费率类型  当payType取值02，时必填 1 民生类 2 一般类  3 餐娱类 4 批发类 5 房产类

    public static final String PAY_AMT                = "payAmt";                   //交易金额

    public static final String TRACK                  = "track";                    //磁道信息  磁道数据格式  2磁信息|3磁信息

    public static final String PINBLK                 = "pinblk";                   //密码密文

    public static final String RANDOM                 = "random";                   //随机数

    public static final String MEDIA_TYPE             = "mediaType";                //01 磁条卡 02 IC卡

    public static final String PERIOD                 = "period";                   //有效期限   MEDIA_TYPE=2 必须

    public static final String IC_DATA                = "icdata";                   //55域   MEDIA_TYPE=2  必须

    public static final String CRD_NUM                = "crdnum";                   //卡片序列号   MEDIA_TYPE=2  必须

    public static final String MAC                    = "mac";                      //设备计算的mac

    public static final String SIGN_FILE_ID           = "signFileId";               //电子签名图片ID

    public static final String CTYPE                  = "ctype";                    //消费类型  00 T0交易  01 T1交易

    public static final String SCAN_CARD_NUM          = "scancardnum";              //电子扫描的银行卡号

    public static final String SCAN_OR_NOT            = "scanornot";                //是否扫描所得  1,扫描  2，手工输入

    public static final String ADDRESS                = "address";                  //消费地址，  省份或城市

    public static final String TX_AMT                 = "txamt";                    //要提现的金额 单位:分

    public static final String CAS_TYPE               = "casType";                  //提现的类型1：T0提现 2：T1提现 3:不区分混合式提现

    public static final String BUS_TYPE               = "busType";                  //业务类型     00：所有，01：收款,02:消费,03:提现

    public static final String PAW_WAY                = "payWay";                   //支付方式  01 支付账户   02 终端   03 快捷支付

    public static final String TRAN_STATE             = "tranState";                //交易状态

    public static final String START_TIME             = "startTime";                //交易开始时间

    public static final String END_TIME               = "endTime";                  //交易结束时间

    public static final String RECORD_TYPE            = "recordType";               //交易记录类型

    public static final String BIZ_TYPE               = "bizType";                  //子订单类型  000003 水  000004 电   000005 然

    public static final String ORD_NO                 = "Ordno";                    //订单号

    public static final String PRDORD_NO              = "prdordNo";                 //商品订单号

    public static final String CONTENT                = "content";                  //内容，  签名图片或电子小票转为Base64字符串

    public static final String BANK_CARD              = "bankcard";                 //银行卡照片

    public static final String CARD_NUM               = "cardnum";                  //银行卡号

    public static final String PRD_NAME               = "prdName";                  //商品名称

    public static final String PRICE                  = "price";                    //商品单价

    public static final String MOBILE                 = "Mobile";                   //手机号

    public static final String CARD_IN_LOWER          = "cardin";                   //转入卡号

    public static final String ACCOUNT_VAL            = "accountVal";               //客户编号

    public static final String COMPANY_ID             = "companyid";                //缴费单位编号

    public static final String ORDER_ID               = "orderid";                  //缴费订单号

    public static final String MONEY                  = "money";                    //转账金额 单位 分

    public static final String CARD_OUT               = "cardout";                  //转出卡号

    public static final String CARD_IN_UPPER          = "Cardin";                   //转入卡号
    
    public static final String ACC_TYPE 			  = "acctType";					//账户类型   
    
    public static final String AMT_ACCT_TYPE_02		  = "amtAcctType02";			//账户类型02提现金额
    
    public static final String AMT_ACCT_TYPE_03		  = "amtAcctType03";			//账户类型02提现金额
    
    public static final String AMT_ACCT_TYPE_04		  = "amtAcctType04";			//账户类型02提现金额

    /****************************************返回参数*********************************************/

    public static final String RESULT_REP_BODY        = "REP_BODY";                 //返回详细

    public static final String RESULT_RSPMSG          = "RSPMSG";                   //消息

    public static final String RESULT_RSPCOD          = "RSPCOD";                   //返回码

    public static final String RESULT_ACT0            = "acT0";                     //及时到账余额

    public static final String RESULT_ACT1            = "acT1";                     //未到账余额

    public static final String RESULT_ACT1_Y          = "acT1Y";                    //隔天到账余额

    public static final String RESULT_ACBAL           = "acBal";                    //账户总余额

    public static final String RESULT_ACT1_AP         ="acT1AP";                    //已审核余额

    public static final String RESULT_ONCREDIT         = "onCredit";                  //挂账

    public static final String RESULT_FREEZE          = "freeze";                   //冻结

    public static final String RESULT_RESERVEFIELD    = "reserveField ";            //与保留字段
    
    public static final String RESULT_PAY_URL         = "payUrl";					//扫码时需要生成的二维码内容
    
    public static final String RESULT_ACT1AP_ACT03    = "acT1AP_ACT03";   			//账户类型03已审核金额
    
    public static final String RESULT_ACT1Y_ACT03	  = "acT1Y_ACT03";				//账户类型03已结算金额----隔天到帐余额
    
    public static final String RESULT_ACT1AP_ACT04	  = "acT1AP_ACT04";				//账户类型04已审核金额
    
    public static final String RESULT_ACT1Y_ACT04	  = "acT1Y_ACT04";				//账户类型04已结算金额----隔天到帐余额
    
    public static final String RESULT_ACT1_UNA 		  = "acT1UNA";					//未审核金额
    
    public static final String RESULT_ACT1_AUNP 	  = "acT1AUNP";					//审核未通过金额
    
    public static final String RESULT_ORD_AMT 		  = "ordamt";					//订单金额
    
    public static final String RESULT_ORD_STATUS	  =	"ordstatus";				//订单状态
    
    public static final String RESULT_ORD_TYPE		  = "ordtype";					//订单类型
    
    public static final String RESULT_ORD_NO		  = "ordno";					//订单号
    
    public static final String RESULT_ORD_TIME		  = "ordtime";					//订单时间
    
    public static final String RESULT_ORD_PAYTYPE	  = "PAYTYPE";					//支付类型
    
    
    
}
