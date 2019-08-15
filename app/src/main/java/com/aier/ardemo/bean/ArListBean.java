package com.aier.ardemo.bean;


import java.util.List;

public class ArListBean {

        /**
         * success : true
         * data : [{"gid":6,"title":"婴儿车","icon":"https://zcc.zq12369.com/upload/2019-08-09/229988556149037.jpeg","displayindex":1,"arkey":"10307873","updatetime":"2019-08-09 15:57:07"},{"gid":7,"title":"智能衣柜","icon":"https://zcc.zq12369.com/upload/2019-08-09/407352970162888.jpeg","displayindex":4,"arkey":" 10308027","updatetime":"2019-08-09 16:02:30"},{"gid":8,"title":"智能床头柜","icon":"https://zcc.zq12369.com/upload/2019-08-09/404460183547393.jpeg","displayindex":3,"arkey":"10307931","updatetime":"2019-08-09 16:11:27"},{"gid":9,"title":"智能双人床","icon":"https://zcc.zq12369.com/upload/2019-08-09/140389513861907.jpeg","displayindex":2,"arkey":" 10307920","updatetime":"2019-08-09 16:06:19"}]
         */

        private boolean success;
        private List<DataBean> data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }



}
