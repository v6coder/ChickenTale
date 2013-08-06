package com.chickentale;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.story.Story;

/**
 * Async task to load stories from JSON files
 */
public class LoadStoryTask extends AsyncTask<AssetManager, Integer, Story>
{
	// Weak reference to the activity 
	private WeakReference<StoryActivity> activityReference;
	
	private String storyFilePath;
	
	/**
	 * Constructs a task to load stories for the given activity
	 * @param activity activity to load the story for
	 */
	LoadStoryTask(StoryActivity activity)
	{
		storyFilePath = activity.getStoryFilePath();
		activityReference = new WeakReference<StoryActivity>(activity);
	}
	
	@Override
	protected void onPreExecute()
	{
		StoryActivity activity = activityReference.get();
		
		// Do not proceed if we have a null activity
		if (activity == null)
		{
			return;
		}
		
		// Reset the progress dialog
		publishProgress(0);
	}
	
	@Override
	protected Story doInBackground(AssetManager... params)
	{
		try
		{
			AssetManager manager = params[0];
			
			if (manager == null)
			{
				return null;
			}
			
			// Initialize the story from a story file path (pointing to a JSON file)
			ObjectMapper mapper = new ObjectMapper();
			InputStream storyStream = manager.open(storyFilePath);
			Story story = mapper.readValue(storyStream, Story.class);
			
			// Fake load time...
			for (int x = 0; x < 100; x++)
			{
				Thread.sleep(10);
				publishProgress(x);
			}
			
			return story;
		}
		catch (Exception e)
		{
			Log.d("LoadStoryTask Error", "Unable to load the story.");
			return null;
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		StoryActivity activity = activityReference.get();
		
		// Do not proceed if we have a null activity
		if (activity == null)
		{
			return;
		}
		
		ProgressDialogFragment progressDialog = (ProgressDialogFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getProgressDialogTag());
		if (progressDialog != null)
		{
			progressDialog.setProgress(progress[0]);	
		}
	}

	@Override
	protected void onPostExecute(Story result)
	{
		StoryActivity activity = activityReference.get();
		
		// Do not proceed if we have a null activity
		if (activity == null)
		{
			return;
		}
		
		// Kill the progress dialog
		ProgressDialogFragment progressDialog = (ProgressDialogFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getProgressDialogTag());
		if (progressDialog != null)
		{
			progressDialog.dismiss();	
		}
		
		
		// If we didn't get a story, kill the app
		if (result == null)
		{
			activity.finish();
			return;
		}
		
		// Update data and views with loaded story
		activity.setStory(result);		
		activity.updateTextViews();
	}
}
