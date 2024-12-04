package com.eyinfo.webx;

import com.eyinfo.foundation.utils.ObjectJudge;
import com.eyinfo.webx.utils.InjectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Runnable> applicationRunners = InjectionUtils.getApplicationRunners();
        if (!ObjectJudge.isNullOrEmpty(applicationRunners)) {
            for (Runnable applicationRunner : applicationRunners) {
                applicationRunner.run();
            }
        }
    }
}
