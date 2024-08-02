"""
This part of code is the Q learning brain, which is a brain of the agent.
All decisions are made in here.

View more on my tutorial page: https://morvanzhou.github.io/tutorials/
"""
import csv
import os
import random
import secrets
import time
import math
import numpy as np
from numpy.random import gumbel
import pandas as pd
import pickle

import uiautomator as ui
import DateBase as db


class QLearningTable:

    def __init__(self, actions, learning_rate=0.01, reward_decay=0.9, e_greedy=0.9):
        self.actions = actions  # a list
        self.lr = learning_rate
        self.gamma = reward_decay
        self.epsilon = e_greedy

        if os.path.exists('./model.csv'):
            if os.path.getsize('./model.csv'):
                print('Q_table file exists and is not empty')
                self.q_table = pd.read_csv('model.csv', header=0, index_col=0)
            else:
                print('Q_table file exists and is empty')
                self.q_table = pd.DataFrame(columns=self.actions, dtype=np.float64)
        else:
            print('Q_table file does not exist')
            fp = open("model.csv", 'w')
            fp.close()
            self.q_table = pd.DataFrame(columns=self.actions, dtype=np.float64)
        print(self.q_table)

        if os.path.exists('./events.pkl'):
            if os.path.getsize('./events.pkl'):
                print('Events file exists and is not empty')
                with open("events.pkl", "rb") as tf:
                    db.events = pickle.load(tf)
            else:
                print('Events file exists and is empty')
        else:
            print('Events file does not exist')
            fp = open("events.pkl", 'w')
            fp.close()
        if os.path.exists('./DFA.pkl'):
            if os.path.getsize('./DFA.pkl'):
                print('DFA file exists and is not empty')
                with open("DFA.pkl", "rb") as df:
                    db.DFA = pickle.load(df)
            else:
                print('DFA file exists and is empty')
        else:
            print('DFA file does not exist')
            fp = open("DFA.pkl", 'w')
            fp.close()
        if os.path.exists('./tabu.pkl'):
            if os.path.getsize('./tabu.pkl'):
                print('Tabu file exists and is not empty')
                with open("tabu.pkl", "rb") as tf:
                    db.T_abu = pickle.load(tf)
            else:
                print('Tabu file exists and is not empty')
        else:
            print('Tabu file does not exist')
            fp = open("tabu.pkl", 'w')
            fp.close()

    def choose_action(self, observation):
        self.check_state_exist(observation)
        if np.random.uniform() < self.epsilon:
            # choose best action
            state_action = self.q_table.loc[observation, :]
            values = state_action.values.tolist()
            print("     Q_value", values)
            action_index = values.index(max(values))
            print("     This action is selected according to the maximum Q.")
        else:
            # choose random action
            # action = np.random.choice(self.actions)
            action_num = len(db.current_event)
            action_index = random.randint(0, action_num - 1)
            print("     The action is based on randomly selected")
        print("     Action selected.")
        return action_index

    def learn(self, s, a, r, s_):
        self.check_state_exist(s_)
        print('learning')
        print(s)
        print(a)
        print(self.q_table.loc[s])
        print(self.q_table.loc[s][a])
        q_predict = self.q_table.loc[s][a]
        if (len(db.current_event)) > 0:
            state_action_ = self.q_table.loc[s_, :]
            values_ = state_action_.values.tolist()
            q_target = r + self.gamma * max(values_)  # next state is not terminal
        else:
            q_target = r  # next state is terminal
        # self.q_table.loc[s][a] = q_target  # WE update
        self.q_table.loc[s][a] += self.lr * (q_target - q_predict)  # AE update
        # save Q-learning model
        self.q_table.to_csv('model.csv')
        with open("events.pkl", "wb") as tf:
            pickle.dump(db.events, tf)
        with open("DFA.pkl", "wb") as tf:
            pickle.dump(db.DFA, tf)
        with open("tabu.pkl", "wb") as tf:
            pickle.dump(db.T_abu, tf)
        print('Saving the model is complete.')
        time.sleep(1)

    def check_state_exist(self, observation):
        rows = list(self.q_table.index)
        print(rows)
        if observation not in rows:
            print("     ", observation, "State is not in the Q-table")
            ui.analysis_action(observation)
            self.add_Q_table(0, observation)
        else:
            print("     ", observation, "State is in the Q-table")
            db.current_event = db.events[observation]
        if len(db.DFA[observation]['actions']) == 0:
            db.DFA[observation]['actions'] = [0]*(len(db.current_event))+[None]*(len(self.actions)-len(db.current_event))

    def add_Q_table(self, q_value, observation):
        # append new state to q table
        # new = pd.DataFrame(columns=self.actions, dtype=np.float64)
        new = [q_value] * (len(db.current_event))+[None]*(len(self.actions)-len(db.current_event))
        # new.loc[observation] = a
        self.q_table.loc[observation] = new
        print(self.q_table)
        # self.q_table = self.q_table.append(
        #     pd.Series(
        #         [q_value] * (len(db.current_event))+[None]*self.actions-(len(db.current_event)),
        #         name=observation,
        #     )
        # )
