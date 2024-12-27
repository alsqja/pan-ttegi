package com.example.panttegi.config.aop;

import com.example.panttegi.common.BaseDtoDataType;
import com.example.panttegi.common.CommonResDto;
import com.example.panttegi.workspace.entity.Workspace;
import com.example.panttegi.workspace.repository.WorkspaceRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;
    private final WorkspaceRepository workspaceRepository;

    private static final String SLACK_API_URL = "https://slack.com/api/chat.postMessage";

    @AfterReturning(
            pointcut = "execution(* com.example.panttegi.workspace.controller.WorkspaceController.*(..))",
            returning = "response"
    )
    public void notifyAfterWorkspaceReturning(JoinPoint joinPoint, Object response) {

        String method = request.getMethod();

        if (method.equals("GET")) {
            return;
        }
        if (request.getRequestURI().equals("/api/workspaces")) {
            return;
        }

        Long workspaceId = Long.parseLong(request.getRequestURI().split("/")[3]);

        if (method.equals("DELETE")) {
            return;
        }

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        if (workspace.getNotifyToken() == null || workspace.getNotifyChannel() == null) {
            return;
        }

        if (response instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();
            log.info("{} Workspace 에서 {} 되었습니다.", workspace.getName(), method);

            String bodyMessage = method;

            if (body instanceof CommonResDto<? extends BaseDtoDataType> commonResDto) {
                bodyMessage = commonResDto.getMessage();
            }

            String message = workspace.getName() + " Workspace 에서 " + bodyMessage + " 되었습니다.";

            callSlackApi(message, workspace);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.example.panttegi.board.controller.BoardController.*(..))",
            returning = "response"
    )
    public void notifyAfterBoardReturning(JoinPoint joinPoint, Object response) {

        Result result = getResult();
        if (result == null) return;

        if (response instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();

            String bodyMessage = result.method;

            if (body instanceof CommonResDto<? extends BaseDtoDataType> commonResDto) {
                bodyMessage = commonResDto.getMessage();
            } else {
                if (result.method.equals("DELETE")) {
                    bodyMessage = "보드 삭제";
                }
            }

            String message = result.workspace.getName() + " 에서 " + bodyMessage + " 되었습니다.";

            callSlackApi(message, result.workspace);
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.example.panttegi.card.controller.CardController.*(..))",
            returning = "response"
    )
    public void notifyAfterCardReturning(JoinPoint joinPoint, Object response) {

        Result result = getResult();
        if (result == null) return;

        if (response instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();

            String bodyMessage = result.method();

            if (body instanceof CommonResDto<? extends BaseDtoDataType> commonResDto) {
                bodyMessage = commonResDto.getMessage();
            } else {
                if (result.method().equals("DELETE")) {
                    bodyMessage = "카드 삭제";
                }
            }

            String message = result.workspace().getName() + " 에서 " + bodyMessage + " 되었습니다.";

            callSlackApi(message, result.workspace());
        }
    }

//    @AfterReturning(
//            pointcut = "execution(* com.example.panttegi.comment.controller.CommentController.*(..))",
//            returning = "response"
//    )

//    @AfterReturning(
//            pointcut = "execution(* com.example.panttegi.list.controller.ListController.*(..))",
//            returning = "response"
//    )

    private @Nullable Result getResult() {
        String method = request.getMethod();

        if (method.equals("GET")) {
            return null;
        }

        Long workspaceId = Long.parseLong(request.getRequestURI().split("/")[3]);

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        if (workspace.getNotifyToken() == null || workspace.getNotifyChannel() == null) {
            return null;
        }
        return new Result(method, workspace);
    }

    private record Result(String method, Workspace workspace) {
    }

    private void callSlackApi(String message, Workspace workspace) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + workspace.getNotifyToken());

        String url = SLACK_API_URL + "?channel=" + workspace.getNotifyChannel() + "&text=" + message;

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("slack 성공");
        } else {
            log.info("slack 실패");
        }
    }
}
