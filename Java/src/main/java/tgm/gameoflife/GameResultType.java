package tgm.gameoflife;

/**
 * Types of game result states. This is a subset of known states as
 * infinite loops [space ships, gliders] are not looked for currently.
 */
public enum GameResultType { DEAD, STEADY, LOOP, UNKNOWN }


