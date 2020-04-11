package com.zoportfolio.checklistproject.Tasklist.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zoportfolio.checklistproject.R;
import com.zoportfolio.checklistproject.Tasklist.Adapters.TasksAdapter;
import com.zoportfolio.checklistproject.Tasklist.DataModels.UserTask;
import com.zoportfolio.checklistproject.Tasklist.DataModels.UserTaskList;

import java.util.ArrayList;

public class TaskListFragment extends Fragment implements TasksAdapter.TasksAdapterListener {

    private static final String TAG = "TaskListFragment.TAG";
    
    //TODO: Implementing the rest of the logic, see TODOs below.

    private static final String ARG_USERTASKLIST = "userTaskList";

    //Views
    private ListView mLvTasks;
    private TextView mTvName;
    private ImageButton mIbEdit;
    private boolean mEditing = false;

    //DataModel
    private UserTaskList mTaskList;

    public static TaskListFragment newInstance(UserTaskList _userTaskList) {
        
        Bundle args = new Bundle();
        args.putSerializable(ARG_USERTASKLIST, _userTaskList);

        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private TaskListFragmentListener mListener;
    public interface TaskListFragmentListener {
        //TODO: rename these callbacks accordingly.
        void taskTapped();

        void editTapped();

        //TODO: Will need a tasklist ID,
        // potential solution = use tasklist name, prevent user from entering duplicate names.
        void taskListUpdated(UserTaskList updatedTaskList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof TaskListFragmentListener) {
            mListener = (TaskListFragmentListener)context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_layout_tasklist, container, false);
        mTvName = view.findViewById(R.id.tv_TaskListTitle);
        mIbEdit = view.findViewById(R.id.ib_Edit);
        mLvTasks = view.findViewById(R.id.lv_tasks);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: main logic for the fragment.
        mTaskList = (UserTaskList) (getArguments() != null ? getArguments().getSerializable(ARG_USERTASKLIST) : null);

        if(getActivity() != null && mTaskList != null) {
            mTvName.setText(mTaskList.getTaskListName());

            //Fill adapter and set it to the listView.
            if(mTaskList.getTasks() != null) {
                TasksAdapter ta = new TasksAdapter(getActivity(), mTaskList.getTasks(), this);
                mLvTasks.setAdapter(ta);
                mLvTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.i(TAG, "onItemClick: Row:" + position + " Task: " + mTaskList.getTasks().get(position).getTaskName());
                    }
                });
            }

            //Set up the editing and edit button AFTER listView is setup.

        }else {
            Log.i(TAG, "onActivityCreated: Tasklist is null.");
        }
    }


    @Override
    public void actionTapped(UserTask userTask, int position) {
        Log.i(TAG, "actionTapped: Task: " + userTask.getTaskName()
                + " \nNotification Time: " + userTask.getTaskNotificationTime()
                + " \nPosition in tasklist: " + position
                + " \nChecked: " + userTask.getTaskChecked());
        //TODO: Update the task that was changed, and then update the tasklist.

        //If true set to false, if false set to true.
        //I have a feeling this may give some problems, will have to keep in mind for later.
        userTask.setTaskChecked(!userTask.getTaskChecked());

        //Grab the tasks from the tasklist.
        //Assign the updated task to the tasks.
        //Set the updated tasks to the tasklist.
        ArrayList<UserTask> tasks = mTaskList.getTasks();
        tasks.set(position, userTask);
        mTaskList.setTasks(tasks);

        //TODO: Need to update the tasklist in terms of local storage.

        //Set the adapter.
        TasksAdapter ta = new TasksAdapter(getActivity(), mTaskList.getTasks(), this);
        mLvTasks.setAdapter(ta);
    }

    @Override
    public void taskTapped(UserTask userTask, int position) {
        //TODO: Open the next activity that is a task info screen... [LATER]
    }

    @Override
    public void addTaskTapped() {
        //TODO: Fill out what happens when the add task is checked.

        //TODO: For testing purposes, going to add 3 tasks to fill out the list and see what happens.
        UserTask newTask1 = new UserTask("Code daily","333", true);
        UserTask newTask2 = new UserTask("ayayaya","222", true);
        UserTask newTask3 = new UserTask("last task","222", true);

        mTaskList.addTaskToList(newTask1);
        mTaskList.addTaskToList(newTask2);
        mTaskList.addTaskToList(newTask3);

        TasksAdapter ta = new TasksAdapter(getActivity(), mTaskList.getTasks(), this);
        mLvTasks.setAdapter(ta);
    }

}
