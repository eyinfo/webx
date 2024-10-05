package com.eyinfo.webx.entity;

import com.eyinfo.foundation.entity.BaseResponse;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/14
 * @Description:验证返回信息
 * @Modifier:
 * @ModifyContent:
 */
public class ValidReturn {
    /**
     * 验证是否通过
     */
    private boolean isPassable = false;
    /**
     * 验证信息
     */
    private BaseResponse response = null;

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean passable) {
        isPassable = passable;
    }

    public BaseResponse getValidResponse() {
        if (response == null) {
            response = new BaseResponse();
        }
        return response;
    }

    public void setValidResponse(BaseResponse response) {
        this.response = response;
    }
}
