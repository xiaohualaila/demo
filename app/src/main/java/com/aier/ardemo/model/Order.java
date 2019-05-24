package com.aier.ardemo.model;

import java.util.List;

/**
 * 作者：leavesC
 * 时间：2018/10/29 21:22
 * 描述：
 * GitHub：https://github.com/leavesC
 * Blog：https://www.jianshu.com/u/9df45b87cfdf
 */
public class Order {

    /**
     * success : true
     * errcode : 0
     * errmsg :
     * result : {"success":true,"data":{"total":3,"data":[{"order_id":"O2019052100007","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:11:50","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100008","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:13:29","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100009","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:15:27","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0002","number":10,"price":"1900.00"}]}]}}
     */

    private boolean success;
    private int errcode;
    private String errmsg;
    private ResultBean result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * success : true
         * data : {"total":3,"data":[{"order_id":"O2019052100007","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:11:50","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100008","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:13:29","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100009","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:15:27","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0002","number":10,"price":"1900.00"}]}]}
         */

        private boolean success;
        private DataBeanX data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public DataBeanX getData() {
            return data;
        }

        public void setData(DataBeanX data) {
            this.data = data;
        }

        public static class DataBeanX {
            /**
             * total : 3
             * data : [{"order_id":"O2019052100007","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:11:50","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100008","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:13:29","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]},{"order_id":"O2019052100009","socialcode":1,"status":"0","total_price":"19000.00","order_time":"2019-05-21 11:15:27","product":[{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0002","number":10,"price":"1900.00"}]}]
             */

            private int total;
            private List<DataBean> data;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * order_id : O2019052100007
                 * socialcode : 1
                 * status : 0
                 * total_price : 19000.00
                 * order_time : 2019-05-21 11:11:50
                 * product : [{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"},{"commodity_id":"c0001","number":10,"price":"1900.00"}]
                 */

                private String order_id;
                private int socialcode;
                private String status;
                private String total_price;
                private String order_time;
                private List<ProductBean> product;

                public String getOrder_id() {
                    return order_id;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public int getSocialcode() {
                    return socialcode;
                }

                public void setSocialcode(int socialcode) {
                    this.socialcode = socialcode;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getTotal_price() {
                    return total_price;
                }

                public void setTotal_price(String total_price) {
                    this.total_price = total_price;
                }

                public String getOrder_time() {
                    return order_time;
                }

                public void setOrder_time(String order_time) {
                    this.order_time = order_time;
                }

                public List<ProductBean> getProduct() {
                    return product;
                }

                public void setProduct(List<ProductBean> product) {
                    this.product = product;
                }

                public static class ProductBean {
                    /**
                     * commodity_id : c0001
                     * number : 10
                     * price : 1900.00
                     */

                    private String commodity_id;
                    private int number;
                    private String price;

                    public String getCommodity_id() {
                        return commodity_id;
                    }

                    public void setCommodity_id(String commodity_id) {
                        this.commodity_id = commodity_id;
                    }

                    public int getNumber() {
                        return number;
                    }

                    public void setNumber(int number) {
                        this.number = number;
                    }

                    public String getPrice() {
                        return price;
                    }

                    public void setPrice(String price) {
                        this.price = price;
                    }
                }
            }
        }
    }
}
