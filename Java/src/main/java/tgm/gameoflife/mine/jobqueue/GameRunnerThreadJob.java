package tgm.gameoflife.mine.jobqueue;

import tgm.utils.ThreadJob;

import java.util.ArrayList;

/**
 * Job object which wraps the task that the thread needs to complete. For something as simple
 * as this, the object is a little overkill.
 */
public class GameRunnerThreadJob extends ThreadJob<ArrayList<ArrayList<Byte>>> {

}
