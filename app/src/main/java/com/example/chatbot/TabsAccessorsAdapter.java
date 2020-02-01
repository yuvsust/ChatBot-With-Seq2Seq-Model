package com.example.chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorsAdapter extends FragmentPagerAdapter {


    public TabsAccessorsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case  0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;
            case  1:
                OfficeFragment officeFragment = new OfficeFragment();
                return officeFragment;
            case  2:
                TeacherFragment teacherFragment = new TeacherFragment();
                return teacherFragment;
            case  3:
                SUST_BotFragment sust_botFragment = new SUST_BotFragment();
                return sust_botFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case  0:
                return "Chats";
            case  1:
                return "Office";
            case  2:
                return "Teachers";
            case  3:
                return "SUST Bot";
            default:
                return null;
        }
    }
}
