package com.example.habithive.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habithive.R;
import com.example.habithive.activities.model.Habit;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {
    private List<Habit> habits;
    public HabitAdapter(List<Habit> habits)
    {
        this.habits = habits;
    }
    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.habitName.setText(habit.getName());
        holder.habitGoal.setText(String.format("0/%s", habit.getGoal())); // Placeholder: Update later
        // TODO: Set icon based on habit name (e.g., "Walk" → walking icon)
        holder.actionButton.setOnClickListener(v -> {
            // TODO: Handle action (e.g., increment progress)
        });
    }
    @Override
    public int getItemCount() {
        return habits != null ? habits.size() : 0;
    }

    public void updateHabits(List<Habit> newHabits) {
        this.habits = newHabits;
        notifyDataSetChanged();
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {
        ImageView habitIcon;
        TextView habitName;
        TextView habitGoal;
        ImageButton actionButton;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitIcon = itemView.findViewById(R.id.habitIcon);
            habitName = itemView.findViewById(R.id.habitName);
            habitGoal = itemView.findViewById(R.id.habitGoal);
            actionButton = itemView.findViewById(R.id.actionButton);
        }
    }
}
