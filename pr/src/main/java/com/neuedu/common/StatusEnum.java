package com.neuedu.common;

public enum StatusEnum {


    NO_LOGIN(100,"未登录"),
    NO_AUTHORITY(101,"无权限操作"),


    PARAM_NOT_EMPTY(1,"参数不能为空"),
    USERNAME_NOT_EMPTY(2,"用户名不能为空"),
    EMAIL_NOT_EMPTY(3,"邮箱不能为空"),
    PHONE_NOT_EMPTY(4,"联系方式不能为空"),
    QUESTION_NOT_EMPTY(5,"密保问题不能为空"),
    ANSWER_NOT_EMPTY(6,"密保答案不能为空"),
    PASSWORD_NOT_EMPTY(7,"密码不能为空"),
    NO_QUESTION_AND_ANSWER(8,"该用户未设置找回密码问题"),
    ANSWER_IS_WRONG(9,"问题答案错误"),
    NO_LOGIN_AND_QUIT(10,"用户未登录，无法获取当前用户信息,强制退出"),
    ROLE_NOT_ADMIN(11,"用户非管理员"),

    USERNAME_EXISTS(31,"用户名已经存在"),
    EMAIL_EXISTS(32,"邮箱已经存在"),
    REGISTER_FAIL(33,"注册失败"),
    USERNAME_NOT_EXISTS(34,"用户名不存在"),
    PASSWORD_INCORRENT(35,"密码错误"),
    CATEGORY_NAME_NOT_EMPTY(36,"类别名称不能为空"),
    CATEGORY_INSERT_FAIL(37,"类别添加失败"),
    CATEGORYID_NOT_EMPTY(38,"类别ID不能为空"),
    CATEGORY_NOT_EXISTS(39,"类别不存在"),
    CATEGORY_UPDATE_FAIL(40,"类别修改失败"),
    CATEGORY_UPDATE_STATUS_FAIL(40,"类别状态修改失败"),

    UPLOAD_FILENAME_NOT_EMPTY(200,"上传的文件名不能为空"),
    PRODUCT_ADD_FAIL(201,"商品添加失败"),
    UPDATE_PRODUCT_NOT_EXISTS(202,"更新的商品不存在"),
    PRODUCT_UPDATE_FAIL(203,"商品更新失败"),
    PRODUCT_NOT_EXISTS(204,"商品已下架或删除"),


    PRODUCT_ADD_CART_FAIL(300,"商品添加购物车失败"),
    PRODUCT_OFFLINEORDELETE_FAIL(301,"商品下架或者删除"),
    UPDATE_PRODUCT_CART_FAIL(301,"更新商品失败"),

    ADDRESS_NOT_EMPTY(400,"收货地址不能为空"),
    USER_CART_EMPTY(401,"用户购物车为空"),
    PRODUCT_STOCK_NOT_FULL(402,"商品库存不足"),
    ORDER_CREATE_FAIL(403,"订单创建失败"),
    ORDER_ITEM_CREATE_FAIL(404,"订单明细创建失败"),
    REDUCE_STOCK_FAIL(405,"扣库存失败"),
    CART_CLEAN_FAIL(406,"购物车删除失败"),
    ORDER_NOT_EXISTS(407,"订单不存在"),
    ORDER_CANT_CANCEL(408,"订单无法删除"),
    ORDER_CANCEL_FAIL(409,"订单取消失败"),
    UPDATE_STOCK_FAIL(410,"更新库存失败"),

    ADDRESS_ADD_FAIL(500,"地址添加失败"),
    ALIPAY_ADD_FAIL(501,"支付宝订单失败"),
    ORDER_STATUS_FAIL(502,"订单状态改变失败"),


    ;


    private  int status; //状态码值
    private String desc;//对状态码值的描述

    StatusEnum(int status,String desc){
        this.status=status;
        this.desc=desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
