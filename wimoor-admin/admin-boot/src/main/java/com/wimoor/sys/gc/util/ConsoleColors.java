package com.wimoor.sys.gc.util;

import lombok.extern.slf4j.Slf4j;


/***
 * log.error 和log 输出颜色
 * @author 王松
 */
@Slf4j
public class ConsoleColors {

    /**
     * Reset 重置颜色
     */
    public static final String RESET = "\033[0m";
    /**
     * PURPLE
     */
    public static final String PURPLE = "\033[0;35m";

    /**
     *  启动成功-紫色
     */
    public static void getSuccessPurple() {
        log.info(ConsoleColors.PURPLE + "\r\n" +
                "         ####                #             #  ##               ##  \n" +
                "     #########        ##### ##             #               #   ##  \n" +
                "      ##   ##        ####   #####          ####        ######  # ##\n" +
                "      #    ##            #########    #######          ############\n" +
                "     ########       ####### ## ##     ###   #  #         ## ####  #\n" +
                "     ##            #####    #  ##      #    ## ##        ##   ##  #\n" +
                "     ##               # #  ##  ##      # ### ###         ###  #   #\n" +
                "     ##########      ##### ##  ##      ## ## ###       ####  ##  ##\n" +
                "    ## ###  ###     #### ###   #      ##  ## ##       ###   ##   ##\n" +
                "    ## ##   ##      ##    ##  ##      ##  # ####            ##   ##\n" +
                "   ##  ##  ##            ## ####     ##  ## #  ## #        ##  ### \n" +
                "  ##   #######          ##   ##      #   #      ###       #    ### \n" +
                "                             #                   ##                "
                + ConsoleColors.RESET);
    }
}
