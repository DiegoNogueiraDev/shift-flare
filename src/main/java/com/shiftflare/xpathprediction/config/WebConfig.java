package com.shiftflare.xpathprediction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração web para gerenciamento de URLs e redirecionamentos
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura redirecionamentos para as páginas das funcionalidades
     * para manter compatibilidade com URLs diretas
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redireciona URLs diretas para as URLs corretas na pasta pages/
        registry.addViewController("/xpath-prediction.html").setViewName("redirect:/pages/xpath-prediction.html");
        registry.addViewController("/code-review.html").setViewName("redirect:/pages/code-review.html");
        registry.addViewController("/migration.html").setViewName("redirect:/pages/migration.html");
        registry.addViewController("/automation.html").setViewName("redirect:/pages/automation.html");
        registry.addViewController("/privacy-policy.html").setViewName("redirect:/pages/privacy-policy.html");
        registry.addViewController("/terms-of-use.html").setViewName("redirect:/pages/terms-of-use.html");
        
        // Novas páginas
        registry.addViewController("/faq.html").setViewName("redirect:/pages/faq.html");
        registry.addViewController("/resources.html").setViewName("redirect:/pages/resources.html");
        registry.addViewController("/team.html").setViewName("redirect:/pages/team.html");
        
        // Redireciona / para index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
} 