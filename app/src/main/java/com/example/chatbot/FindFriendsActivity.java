//package com.example.chatbot;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.appcompat.widget.Toolbar;
//import androidx.viewpager.widget.ViewPager;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.squareup.picasso.Picasso;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//public class FindFriendsActivity extends AppCompatActivity {
//
//    private Toolbar mToolbar;
//    private RecyclerView FindFriendsRecyclerList;
//    private DatabaseReference userRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_find_friends);
//
//        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        FindFriendsRecyclerList = findViewById(R.id.find_friend_recycler_list);
//        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
//
//        mToolbar = findViewById(R.id.find_friends_toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("Find Teachers");
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions <Contacts> options =
//                new FirebaseRecyclerOptions.Builder<Contacts>()
//                .setQuery(userRef, Contacts.class)
//                .build();
//        FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder> adapter =
//                new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
//                    @Override
//                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Contacts model) {
//                        holder.userName.setText(model.getName());
//                        holder.userStatus.setText(model.getStatus());
//                        Picasso.get().load(model.getImage()).into(holder.profileImage);
//                    }
//
//                    @NonNull
//                    @Override
//                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout, parent, false);
//                        FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
//                        return viewHolder;
//                    }
//                };
//
//        FindFriendsRecyclerList.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
//
//        TextView userName, userStatus;
//        CircleImageView profileImage;
//        public FindFriendsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            userName = itemView.findViewById(R.id.user_profile_name);
//            userStatus = itemView.findViewById(R.id.user_online_status);
//            profileImage = itemView.findViewById(R.id.users_profile_image);
//
//        }
//    }
//}
