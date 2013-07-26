package com.story;

/**
 * A story consists of a brief description for its entry action, a description of the story itself, and references to two other stories
 */
public class Story
{
	private String actionDescription;
	private String storyDescription;
	private Story firstAction;
	private Story secondAction;
	
	/**
	 * Default constructor that does nothing
	 */
	public Story()
	{ }
	
	/**
	 * Gets the description of the action leading to this story
	 * @return the description of the action leading to this story
	 */
	public String getActionDescription()
	{
		return actionDescription;
	}
	
	/**
	 * Sets the description of the action leading to this story
	 * @param actionDescription description fo the action leading to this story
	 */
	public void setActionDescription(String actionDescription)
	{
		this.actionDescription = actionDescription;
	}
	
	/**
	 * Gets the description of this story
	 * @return the description of this story
	 */
	public String getStoryDescription()
	{
		return storyDescription;
	}
	
	/**
	 * Sets the description of this story
	 * @param storyDescription the description of this story
	 */
	public void setStoryDescription(String storyDescription)
	{
		this.storyDescription = storyDescription;
	}
	
	/**
	 * Gets the first action of this story
	 * @return the first action of this story
	 */
	public Story getFirstAction()
	{
		return firstAction;
	}
	
	/**
	 * Sets the first action of this story
	 * @param firstAction the first action of this story
	 */
	public void setFirstAction(Story firstAction)
	{
		this.firstAction = firstAction;
	}
	
	/**
	 * Gets the second action of this story
	 * @return the second action of this story
	 */
	public Story getSecondAction()
	{
		return secondAction;
	}
	
	/**
	 * Sets the second action of this story
	 * @param secondAction the second action of this story
	 */
	public void setSecondAction(Story secondAction)
	{
		this.secondAction = secondAction;
	}
	
	/**
	 * Checks if we have no more actions for continuing the story
	 * @return true if we have no way to continue the story
	 */
	public boolean isTheEnd()
	{
		return firstAction == null && secondAction == null;
	}
	
	@Override
	public String toString()
	{
		return String.format("ChickenStory [actionDescription=%s, storyDescription=%s, firstAction=%s, secondAction=%s]",
							 actionDescription,
							 storyDescription,
							 firstAction,
							 secondAction);
	}
}
