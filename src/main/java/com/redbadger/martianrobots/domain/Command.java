package com.redbadger.martianrobots.domain;

/**
 * An instruction a robot can carry out.
 */
public enum Command {
    /** Turn left, ninety degrees anticlockwise. */
    L,
    /** Turn right, ninety degrees clockwise. */
    R,
    /** Move one square forward in the direction currently faced. */
    F
}
