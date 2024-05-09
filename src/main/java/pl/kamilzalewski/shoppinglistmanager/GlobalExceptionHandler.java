package pl.kamilzalewski.shoppinglistmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.kamilzalewski.shoppinglistmanager.admin.LogFileNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.item.ItemNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.jwt.AccessToTheListException;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenValidityException;
import pl.kamilzalewski.shoppinglistmanager.jwt.TokenNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.password.IncorrectPasswordException;
import pl.kamilzalewski.shoppinglistmanager.share.ImpossibleShareException;
import pl.kamilzalewski.shoppinglistmanager.share.ShareNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.shoppinglist.ShoppingListNotFoundException;
import pl.kamilzalewski.shoppinglistmanager.user.AccountAlreadyEnabledException;
import pl.kamilzalewski.shoppinglistmanager.user.UserAlreadyExistException;
import pl.kamilzalewski.shoppinglistmanager.user.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validationExceptionsHandler(MethodArgumentNotValidException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail userNotFoundExceptionHandler(UserNotFoundException e) {
        LOGGER.error("UserNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ProblemDetail userAlreadyExistExceptionHandler(ShoppingListNotFoundException e) {
        LOGGER.error("ShoppingListNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(AccountAlreadyEnabledException.class)
    public ProblemDetail accountAlreadyEnabledExceptionHandler(AccountAlreadyEnabledException e) {
        LOGGER.error("AccountAlreadyEnabledException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ProblemDetail incorrectPasswordExceptionHandler(IncorrectPasswordException e) {
        LOGGER.error("IncorrectPasswordException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ProblemDetail tokenNotFoundExceptionHandler(TokenNotFoundException e) {
        LOGGER.error("TokenNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(TokenValidityException.class)
    public ProblemDetail tokenValidityExceptionHandler(TokenValidityException e) {
        LOGGER.error("TokenValidityException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    public ProblemDetail shoppingListNotFoundExceptionHandler(ShoppingListNotFoundException e) {
        LOGGER.error("ShoppingListNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AccessToTheListException.class)
    public ProblemDetail accessToTheListExceptionHandler(AccessToTheListException e) {
        LOGGER.error("AccessToTheListException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ProblemDetail itemNotFoundExceptionHandler(ItemNotFoundException e) {
        LOGGER.error("ItemNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ShareNotFoundException.class)
    public ProblemDetail shareNotFoundException(ShareNotFoundException e) {
        LOGGER.error("ShareNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(ImpossibleShareException.class)
    public ProblemDetail impossibleShareExceptionHandler(ImpossibleShareException e) {
        LOGGER.error("UImpossibleShareException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(LogFileNotFoundException.class)
    public ProblemDetail logFileNotFoundExceptionHandler(LogFileNotFoundException e) {
        LOGGER.error("LogFileNotFoundException occurred" + e.getMessage());
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }
}
