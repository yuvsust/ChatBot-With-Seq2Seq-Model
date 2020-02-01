package com.example.chatbot;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherFragment extends Fragment {


    private View teachersFragmentView;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_teachers = new ArrayList<>();
    private DatabaseReference teacherRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Toolbar mToolbar;
    private RecyclerView FindFirendsRecyclerList;
    public TeacherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FindFirendsRecyclerList = getView().findViewById(R.id.find_friend_recycler_list);
        FindFirendsRecyclerList.setLayoutManager(new LinearLayoutManager((AppCompatActivity)getActivity()));

        mToolbar = getView().findViewById(R.id.find_friends_toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();
//        teachersFragmentView =  inflater.inflate(R.layout.fragment_teacher, container, false);
//        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        InitializeFields();
//        RetrieveAndDisplayTeachers();
//
////        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String teacherName = parent.getItemAtPosition(position).toString();
////                Intent intent = new Intent(getContext(), ChatWithTeacherActivity.class);
////                intent.putExtra("teacherName", teacherName);
////                startActivity(intent);
////            }
////        });
        return  teachersFragmentView;
    }

//    private void RetrieveAndDisplayTeachers() {
//        teacherRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<String> set = new ArrayList<>();
//                String st, tmp;
//                String[] res;
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while(iterator.hasNext()) {
//
//                    st = ((DataSnapshot)iterator.next()).getValue().toString();
//                    res = st.split(",", 3);
//                    st = res[1].substring(6);
//                    tmp = res[0].substring(5);
//                    if(st.equals("SUST Office") || tmp.equals(currentUser.getUid().toString()))
//                        continue;
//                    set.add(st);
//                }
//                list_of_teachers.clear();
//                list_of_teachers.addAll(set);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void InitializeFields() {
//        listView = (ListView) teachersFragmentView.findViewById(R.id.list_view);
//        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list_of_teachers);
//        listView.setAdapter(arrayAdapter);
//    }

}
