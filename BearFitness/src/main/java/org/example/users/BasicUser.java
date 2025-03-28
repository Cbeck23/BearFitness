package org.example.users;

import java.util.List;

public class BasicUser extends User {

    List<ExercisePlan> plans;

    public BasicUser(String username, String password, String email) {
        super(username, password, email);
    }

    public List<ExercisePlan> getPlans() {
        return plans;
    }

    public void addPlan(ExercisePlan plan) {
        plans.add(plan);
    }

    public void createPlan(){
        boolean done = false;

        while(!done){
            //add workout entrys to plan, save plans

        }
    }


}