package com.perrchick.dbapplication;

import android.location.Location;
import android.widget.ArrayAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class PlayerList extends ArrayAdapter<Player> {
        private Activity context;
        List<Player> players;

        public PlayerList(Activity context, List<Player> players) {
            super(context, R.layout.layout_player_list, players);
            this.context = context;
            this.players = players;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_player_list, null, true);

            TextView textViewRate = (TextView) listViewItem.findViewById(R.id.textViewRate);
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
            TextView textViewScore = (TextView) listViewItem.findViewById(R.id.textViewScore);
            TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);

            Player player = players.get(position);

            textViewRate.setText(""+player.getRate());
            textViewName.setText(player.getName());
            textViewScore.setText("Score: "+player.getScore());
            textViewTime.setText("Time: "+player.getTime());


            return listViewItem;
        }
    }

