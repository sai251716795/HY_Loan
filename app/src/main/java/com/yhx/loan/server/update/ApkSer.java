package com.yhx.loan.server.update;

public class ApkSer {
        public String type;            //更新内容类型，apk安装包  res资源
        public String apkName;
        public String packageName;        //apk包名
        public Long versionCode;     //库版本号
        public String versionName;     //库版本名称
        public int important;          //更新等级 0:普通 1:重点bug修复更新  2:新功能更新 3:配置更新 4:特殊更新，强制用户更新
        public String commit;          //更新内容
        public String updatePath;      //更新下载地址
        public String updateTime;        //更新时间

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getApkName() {
                return apkName;
        }

        public void setApkName(String apkName) {
                this.apkName = apkName;
        }

        public String getPackageName() {
                return packageName;
        }

        public void setPackageName(String packageName) {
                this.packageName = packageName;
        }

        public Long getVersionCode() {
                return versionCode;
        }

        public void setVersionCode(Long versionCode) {
                this.versionCode = versionCode;
        }

        public String getVersionName() {
                return versionName;
        }

        public void setVersionName(String versionName) {
                this.versionName = versionName;
        }

        public int getImportant() {
                return important;
        }

        public void setImportant(int important) {
                this.important = important;
        }

        public String getCommit() {
                return commit;
        }

        public void setCommit(String commit) {
                this.commit = commit;
        }

        public String getUpdatePath() {
                return updatePath;
        }

        public void setUpdatePath(String updatePath) {
                this.updatePath = updatePath;
        }

        public String getUpdateTime() {
                return updateTime;
        }

        public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
        }
}