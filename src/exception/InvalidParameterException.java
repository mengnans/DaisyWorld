package exception;

/**
 * @author Mengnan Shi
 * @create 2018-05-03-11:40
 */

public class InvalidParameterException extends Exception{

    public InvalidParameterException(String errorMessage){
        super(errorMessage);
    }

}
