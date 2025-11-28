package com.wimoor.finance.voucher.service.util;


import lombok.Data;

// 过账结果
@Data
public class PostingResult {

    private boolean success;
    private String message;

    public PostingResult(boolean b, String message) {
        this.success = b;
        this.message = message;
    }


    public static PostingResult success(String message) {
        return new PostingResult(true, message);
    }

    public static PostingResult fail(String message) {
        return new PostingResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

}
