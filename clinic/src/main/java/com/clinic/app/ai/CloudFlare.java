package com.clinic.app.ai;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.bbs.Result;
import com.clinic.app.search.GlobalSearch;
import com.google.common.util.concurrent.AsyncFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping
public class CloudFlare {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {

        private String role;

        private String content;
    }

    @GetMapping("/ai/cf")
    public Result<String> quiz(@RequestParam String quiz) {
        List<Message> messages = Arrays.asList(new Message(
                "system",
                "你是一个医疗软件，需要回答一些医疗相关问题或者需要软件中的某个功能、或者查询一些功能的数据，例如需要类似系统中的某个功能，就引导去使用具体功能，功能有门诊日志（全部接诊记录）、正在接诊（今天未接诊的病人）、新增患者（创建病人档案）、收费、零售、库存、消杀、消毒、诊断证明"
        ), new Message(
                "user",
                quiz
        ));
        TimeInterval timer = DateUtil.timer();
        String resultStr = HttpRequest.post("https://api.cloudflare.com/client/v4/accounts/f7c5e1e128be702e007f4eec33650795/ai/run/@cf/qwen/qwen1.5-14b-chat-awq")
                .header("Authorization", "Bearer Iu0h_tO72-4U0whjsFZ6T2egyD8jo4OHqMcEZrzM")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.toJsonPrettyStr(new HashMap<String, Object>() {{
                    put("messages", messages);
                }}))//表单内容
                .execute().body();
        log.info("AI: resultStr={};", resultStr);
        if(StringUtils.isNotBlank(resultStr)) {
            Object resultObj = JSONUtil.parseObj(resultStr).get("result");
            if(nonNull(resultObj)) {
                String answer = JSONUtil.parseObj(resultObj).get("response").toString();
                log.info("AI: quiz={}; answer={}; timer={};", quiz, answer, timer.interval() / 1000);
                return Result.success(answer);
            }
        }
        log.error("AI: quiz={}; resultStr={}", quiz, resultStr);
        return Result.failed(resultStr);
    }

    @Async("asyncMethodThreadPool")
    public void asyncQuiz(String question, GlobalSearch.VO vo, CountDownLatch countDownLatch) {
        vo.setAnswer(quiz(question).getData());
        countDownLatch.countDown();
    }
}
