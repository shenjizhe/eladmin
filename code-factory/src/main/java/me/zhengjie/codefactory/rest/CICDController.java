package me.zhengjie.codefactory.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.ErrorCode;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.service.CICDService;
import me.zhengjie.codefactory.service.ComponentService;
import me.zhengjie.codefactory.service.GitlabService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

/**
 * 持续集成 的接口
 *
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/11 17:05
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "持续集成")
@RequestMapping("/api/cicd")
public class CICDController {
    private final CICDService cicdService;
    @Log("下载工程")
    @ApiOperation("上传工程")
    @PostMapping(value = "/download-project/{component-id}")
    @PreAuthorize("@el.check('component:add')")
    public Result download(@PathVariable("component-id") Long componentId) throws IOException {
        return cicdService.downloadComponent(componentId);
    }

    @Log("查询进度")
    @ApiOperation("查询进度")
    @PreAuthorize("@el.check('component:add')")
    @GetMapping(value = "/progress", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> getProgress() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(Flux.interval(Duration.ofSeconds(5))
                        .map(sec -> String.format("data: %s:%s",sec, calculateProgress()))
                );
    }

    @GetMapping(value = "/progress1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getProgress1() {
        return Flux.create(sink -> {
            // 获取处理进度，并将数据推送到消费者
            for(int i = 0; i < 10; i++){
                sink.next("Processing: " + i + "%");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sink.complete(); // 处理完成后，关闭事件流
        });
    }

    private String calculateProgress(){
        return UUID.randomUUID().toString();
    }
}
