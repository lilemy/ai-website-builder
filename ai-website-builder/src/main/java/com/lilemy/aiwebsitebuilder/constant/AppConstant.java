package com.lilemy.aiwebsitebuilder.constant;

/**
 * 应用常量
 *
 * @author lilemy
 * @date 2026-03-17 21:41
 */
public interface AppConstant {

    /**
     * 精选应用优先级
     */
    Integer CHOICENESS_APP_PRIORITY = 99;

    /**
     * 默认应用优先级
     */
    Integer DEFAULT_APP_PRIORITY = 0;

    /**
     * 应用生成目录
     */
    String CODE_OUTPUT_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 应用部署目录
     */
    String CODE_DEPLOY_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_deploy";

    /**
     * 应用部署域名
     */
    String CODE_DEPLOY_HOST = "http://localhost";
}
