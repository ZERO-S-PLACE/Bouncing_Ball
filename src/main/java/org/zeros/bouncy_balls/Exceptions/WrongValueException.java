package org.zeros.bouncy_balls.Exceptions;

public class WrongValueException extends UnsupportedOperationException {

        public WrongValueException(double intervalStart, double intervalEnd, double number) {
            super("Wrong value, Interval: "+intervalStart+" : "+intervalEnd+" Value: "+number);
        }
}
