package com.todoapp.todo_backend.exception;

import com.todoapp.todo_backend.dto.responseDto.ErrorResponseDto;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppControllerAdviser {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({ChangeSetPersister.NotFoundException.class})
    public ErrorResponseDto readableNotFound(Exception e){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        return errorResponseDto;
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ExceptionHandler({CustomException.class})
    public ErrorResponseDto customException(Exception e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setMessage(e.getMessage());
        return errorResponseDto;
    }

}
