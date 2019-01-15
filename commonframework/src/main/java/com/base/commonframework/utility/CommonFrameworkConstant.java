package com.base.commonframework.utility;

/**
 * CommonFrameworkConstant Class :
 *
 * @author Roshan Kumar
 * @version 1.0
 * @since 26/10/18
 */
public interface CommonFrameworkConstant {

    int ADD_STAR = 55;

    interface StatusCode {
        interface ShowSeries {
            int SUCCESS = 201;
            int FAILURE = 401;
            int ALREADY_LOGIN = 104;
            int SESSION_EXPIRE = 109;
        }

        interface NotShowSeries {
            int SUCCESS = 200;
            int FAILURE = 400;
        }
    }

}
