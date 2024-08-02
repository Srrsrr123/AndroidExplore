import math

from RL_brain import QLearningTable
import uiautomator as ui
import time
import DateBase as db
from copy import deepcopy


def add_state(dfa, key_a, key_b, val):
    if key_a in dfa.keys():
        dfa[key_a].update({key_b: val})
    else:
        dfa.update({key_a: {key_b: val}})


def update():
    for episode in range(100):
        cur_step = 0
        observation, ob_structure = ui.get_init_state()
        if observation in db.DFA.keys():
            db.DFA[observation]['count'] += 1
        else:
            db.DFA[observation] = {}
            db.DFA[observation]['count'] = 1
            db.DFA[observation]['path'] = {}
            db.DFA[observation]['actions'] = {}
        print("Getting the initial state")
        print("--------------------------------------------------------------------------------------------------")
        state_step = 0
        while True:
            print("Current state:", observation)
            # RL choose action based on observation
            print(">>>Select the action.......")
            action_index = RL.choose_action(observation)
            # RL take action and get next observation and reward
            action, action_type = ui.get_cur_action(action_index)
            if observation not in db.T_abu:
                db.T_abu[observation] = []
            if action_index in db.T_abu[observation]:
                print("This is an end event and will not be executed")
            else:
                print(">>>Starts executing the action. The type is:", action['type'], action['act'][0]['@id'], "class:",
                      action['act'][0]['@class'])
                #       "content-desc:", action['act'][0]['@content-desc'])
                if action != "":
                    ui.excute_action(action, action_type)
                    if db.device.app_current()['package'] != db.AUT:
                        print("The action is executed and then goes to another app, the execution system returns")
                        db.device.press("back")
                        time.sleep(5)
                        if db.device.app_current()['package'] != db.AUT:
                            ui.start_app(db.AUT)
                            if action_index in db.T_abu[observation]:
                                print("This unknown event has been added")
                            else:
                                db.T_abu[observation].append(action_index)
                            time.sleep(7)
                    print("     Action execution is complete, sleep for 2 second and wait for the page to load completely.")
                    time.sleep(2)
            db.DFA[observation]['actions'][action_index] += 1
            observation_, ob_structure_, reward = ui.after_action_reward_state()
            # calculate reward（WebExplor）
            # if (observation, action_index, observation_) in db.N:
            #     db.N[(observation, action_index, observation_)] += 1
            # else:
            #     db.N[(observation, action_index, observation_)] = 1
            # reward = 1.0/math.sqrt(db.N[(observation, action_index, observation_)])
            print("     Reward for completed actions:    ", reward)
            if observation_ in db.DFA.keys():
                state_step += 1
                db.DFA[observation_]['count'] += 1
            else:
                state_step = 0
                db.DFA[observation_] = {}
                db.DFA[observation_]['count'] = 1
                db.DFA[observation_]['path'] = deepcopy(db.DFA[observation]['path'])
                db.DFA[observation_]['path'][len(db.DFA[observation]['path']) + 1] = deepcopy(action)
                db.DFA[observation_]['actions'] = []
            cur_step += 1
            # RL learn from this transition
            print("     next state: ", observation_)
            print(">>>update Q-learning")
            RL.learn(observation, action_index, reward, observation_)
            print("     Q-learning update completed")
            # swap observation
            observation = observation_
            ob_structure = ob_structure_
            print("--------------------------------------------------------------------------------------------------")
            if (cur_step >= db.episode_max_step) or (len(db.current_event) <= 0):
                break

            if state_step > db.DFA_max_step:
                print("--------Beginning DFA Orientation--------")
                min_a = 0
                min_state = ''
                for s in db.DFA.keys():
                    min_state = s
                    break
                for a in db.DFA.keys():
                    temp = db.DFA[a]['actions'].index(min(db.DFA[a]['actions'][0:len(db.events[a])]))
                    temp_s = a
                    if db.DFA[temp_s]['actions'][temp] < db.DFA[min_state]['actions'][min_a]:
                        min_a = temp
                        min_state = temp_s
                observation = min_state
                while True:
                    ob, ob_ = ui.get_init_state()
                    if ob == db.start_state:
                        time.sleep(2)
                        print("Reached the first page")
                        break
                    else:
                        print("Step back")
                        db.device.press("back")
                        time.sleep(2)
                paths = db.DFA[min_state]['path']
                for i in paths:
                    ac = paths[i]
                    ac_type = ac['type']
                    print(">>>Starts executing the action. The type is:", ac['type'], ac['act'][0]['@id'], "class:",
                          ac['act'][0]['@class'])
                    if ac != "":
                        ui.excute_action(ac, ac_type)
                        print("     Action execution is complete, sleep for 2 second and wait for the page to load completely.")
                        time.sleep(2)
                print("target state has been reached")
                if min_a in db.T_abu[observation]:
                    print("This is an end event and will not be executed")
                else:
                    min_ac = db.events[min_state][min_a]
                    min_ac_type = min_ac['type']
                    print(">>>Starts executing the action. The type is:", min_ac['type'], min_ac['act'][0]['@id'], "class:",
                          min_ac['act'][0]['@class'])
                    if min_ac != "":
                        ui.excute_action(min_ac, min_ac_type)
                        if db.device.app_current()['package'] != db.AUT:
                            print("After the action is executed, it enters the other app, and the return action is executed.")
                            db.device.press("back")
                            time.sleep(5)
                            if db.device.app_current()['package'] != db.AUT:
                                ui.start_app(db.AUT)
                                if min_a in db.T_abu[observation]:
                                    print("This unknown event has been added")
                                else:
                                    db.T_abu[observation].append(min_a)
                                time.sleep(7)
                        print("     Action execution is complete, sleep for 2 second and wait for the page to load completely.")
                        time.sleep(2)
                db.DFA[min_state]['actions'][min_a] += 1
                observation_, ob_structure_, reward = ui.after_action_reward_state()
                # if (observation, min_a, observation_) in db.N:
                #     db.N[(observation, min_a, observation_)] += 1
                # else:
                #     db.N[(observation, min_a, observation_)] = 1
                # reward = 1.0 / math.sqrt(db.N[(observation, min_a, observation_)])
                print("     Reward for completed actions:    ", reward)
                if observation_ in db.DFA.keys():
                    state_step += 1
                    db.DFA[observation_]['count'] += 1
                else:
                    state_step = 0
                    db.DFA[observation_] = {}
                    db.DFA[observation_]['count'] = 1
                    db.DFA[observation_]['path'] = deepcopy(db.DFA[observation]['path'])
                    db.DFA[observation_]['path'][len(db.DFA[observation]['path']) + 1] = deepcopy(min_ac)
                    db.DFA[observation_]['actions'] = []
                cur_step += 1
                # RL learn from this transition
                print("     next state: ", observation_)
                print(">>>update Q-learning")
                RL.learn(observation, min_a, reward, observation_)
                print("     Q-learning update complete")
                state_step = len(db.DFA[observation]['path'])
                observation = observation_
                print(
                    "--------------------------------------------------------------------------------------------------")
                if (cur_step >= db.episode_max_step) or (len(db.current_event) <= 0):
                    break
        time.sleep(30)
    # end of game
    print('game over')


if __name__ == "__main__":
    ui.start_app(db.AUT)
    RL = QLearningTable(actions=list(range(60)))
    ob, ob_ = ui.get_init_state()
    db.start_state = ob
    update()
