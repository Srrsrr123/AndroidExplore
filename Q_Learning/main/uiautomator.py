import json
from copy import deepcopy
import xmltodict
import re
import requests
import time
import DateBase as db
import random


def start_app(package):
    db.device.app_start(package)


def stop_app(package):
    db.device.app_stop(package)


def http_post(url, data):
    response = requests.post(url, data=data)
    return response


def get_init_state():
    response = http_post("http://127.0.0.1:8888/curstate", {})
    result = json.loads(response.text)
    return result['stateId'], result['state']


def after_action_reward_state():
    response = http_post("http://localhost:8888/getstate", {})
    result = json.loads(response.text)
    db.reward = result['reward']
    print("     The highest similarity to the set of historical states is:", result['similarity'])
    return result['stateId'], result['state'], db.reward


def analysis_action(observation):
    print("     >>>Start analyzing interface actions.......")
    db.current_event = []
    db.events[observation] = []
    xml = db.device.dump_hierarchy()
    filename = './pages/' + observation + '.xml'
    with open(filename, 'w', encoding="utf-8") as f:
        f.write(xml)
    f.close()
    with open(filename, 'r', encoding="utf-8") as f:
        line = f.readline()
        while line:
            if re.match('<node ', line.strip()) is not None:
                new_line = list(line.strip())
                if new_line[len(new_line) - 2] == '/':
                    new_line[len(new_line) - 2] = ''
                line = ''.join(new_line) + "</node>"
                node_dict = xmltodict.parse(line)
                node = node_dict['node']
                if node['@package'] == db.AUT and node['@enabled'] == 'true' and node['@visible-to-user'] == 'true':
                    analysis_click(node)
                    analysis_long_click(node)
                    analysis_slider(node)
            line = f.readline()
        db.events[observation] = deepcopy(db.current_event)
        print("     Number of actions", len(db.current_event))
    f.close()


def analysis_combination_event(xml):
    if db.AUT == 'com.android.keepass':
        print("Analyzing combo events for com.android.keepass")
        analysis_keepassdroid(xml)


def analysis_keepassdroid(xml):
    event = {}
    act = []
    index = '10'
    password = 'cyr'
    if 'com.android.keepass:id/create' in xml:
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/file_filename',
                    'text': '/storage/emulated/0/keepass/' + index + '.kdbx'})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/create'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/file_filename',
                    'text': '/storage/emulated/0/keepass/' + index + '.kdbx'})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/open'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/file_filename',
                    'text': '0'})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/create'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/file_filename',
                    'text': '0'})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/open'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
    elif 'com.android.keepass:id/pass_conf_password' in xml:
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/pass_password',
                    'text': password})
        act.append({'type': 'input', 'id': 'com.android.keepass:id/pass_conf_password',
                    'text': password})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/ok'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
    elif 'com.android.keepass:id/password' in xml:
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/password',
                    'text': password})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/pass_ok'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
    elif 'com.android.keepass:id/group_name' in xml and 'android.widget.EditText' in xml:
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/group_name',
                    'text': 'group_' + index})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/ok'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
    elif 'com.android.keepass:id/entry_user_name' in xml and 'com.android.keepass:id/entry_password' in xml:
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_title',
                    'text': index})
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_user_name',
                    'text': 'cyr'})
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_url',
                    'text': '/cyr/test'})
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_password',
                    'text': password})
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_confpassword',
                    'text': password})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/entry_save'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
        act.clear()
        event['type'] = 'combination'
        act.append({'type': 'input', 'id': 'com.android.keepass:id/entry_title',
                    'text': ''})
        act.append({'type': 'click', 'id': 'com.android.keepass:id/entry_save'})
        event['act'] = act[:]
        db.current_event.append(dict(**event))
    # pprint.pprint(db.current_event)


def set_event(x, y, input_str, direction, direction_len, long_click_time, id, cls, text, index):
    a = {'@x': x, '@y': y, '@input': input_str, '@direction': direction, '@direction_len': direction_len,
         '@long_click_time': long_click_time, '@id': id, '@class': cls, '@text': text, '@index': index}
    return deepcopy(a)


def get_x_y(node):
    pos = get_view_bounds(node)
    x = (pos[1][0] + pos[0][0]) / 2
    y = (pos[1][1] + pos[0][1]) / 2
    return x, y


def analysis_click(node):
    if node['@clickable'] == 'true':
        if 'EditText' not in node['@class']:
            x, y = get_x_y(node)
            a = set_event(x, y, "", "", 0, 0, node['@resource-id'], node['@class'], node['@text'], node['@index'])
            event = {}
            act = [a]
            event['type'] = 'click'
            event['act'] = act[:]
            db.current_event.append(dict(**event))
        else:
            input_str = "1111"
            x, y = get_x_y(node)
            a = set_event(x, y, input_str, "", 0, 0, node['@resource-id'], node['@class'], node['@text'],
                          node['@index'])
            event_1 = {}
            act_1 = [a]
            event_1['type'] = 'edit'
            event_1['act'] = act_1[:]
            db.current_event.append(dict(**event_1))


def analysis_long_click(node):
    if node['@long-clickable'] == 'true' and 'EditText' not in node['@class']:
        x, y = get_x_y(node)
        a = set_event(x, y, "", "", 0, 1, node['@resource-id'], node['@class'], node['@text'], node['@index'])
        event = {}
        act = [a]
        event['type'] = 'long-click'
        event['act'] = act[:]
        db.current_event.append(dict(**event))


def analysis_scroll(node):
    if node['@scrollable'] == 'true' and 'Spinner' not in node['@class']:
        x, y = get_x_y(node)
        direction = 'y'
        direction_len = 1
        a = set_event(x, y, "", direction, direction_len, 0, node['@resource-id'], node['@class'], node['@text'],
                      node['@index'])
        event = {}
        act = [a]
        event['type'] = 'scrollable'
        event['act'] = act[:]
        db.current_event.append(dict(**event))


def analysis_slider(node):
    if 'SeekBar' in node['@class'] and node['@clickable'] == 'false':
        x, y = get_x_y(node)
        direction = 'x'
        direction_len = 1
        a = set_event(x, y, "", direction, direction_len, 0, node['@resource-id'], node['@class'], node['@text'],
                      node['@index'])
        event = {}
        act = [a]
        event['type'] = 'slider'
        event['act'] = act[:]
        db.current_event.append(dict(**event))


def get_cur_action(action_index):
    action = db.current_event[action_index]
    action_type = action['type']
    return action, action_type


def excute_action(action, action_type):
    if action_type == "click":
        x = action['act'][0]['@x']
        y = action['act'][0]['@y']
        db.device.click(x, y)
    elif action_type == "edit":
        input_str = action['act'][0]['@input']
        if db.device(resourceId=action['act'][0]['@id']).exists:
            if action['act'][0]['@id'] == "com.example.asd:id/Date":
                x = action['act'][0]['@x']
                y = action['act'][0]['@y']
                db.device.click(x, y)
            else:
                db.device(resourceId=action['act'][0]['@id']).send_keys(input_str)
        else:
            db.device.press("volume_up")
    elif action_type == "long-click":
        x = action['act'][0]['@x']
        y = action['act'][0]['@y']
        long_click_time = action['act'][0]['@long_click_time']
        db.device.long_click(x, y, long_click_time)
    elif action_type == "scrollable":
        x = action['act'][0]['@x']
        y = action['act'][0]['@y']
        len = action['act'][0]['@direction_len']
        if db.device(resourceId=action['act'][0]['@id'], className=action['act'][0]['@class'],
                     text=action['act'][0]['@text'], index=action['act'][0]['@index']).exists:
            if action['act'][0]['@direction'] == 'x':
                db.device(resourceId=action['act'][0]['@id'], className=action['act'][0]['@class'],
                          text=action['act'][0]['@text'], index=action['act'][0]['@index']).swipe("left", len)
            else:
                db.device(resourceId=action['act'][0]['@id'], className=action['act'][0]['@class'],
                          text=action['act'][0]['@text'], index=action['act'][0]['@index']).swipe("down", len)
        else:
            db.device.press("volume_up")
    elif action_type == "slider":
        x = action['act'][0]['@x']
        y = action['act'][0]['@y']
        len = action['act'][0]['@direction_len']
        if db.device(resourceId=action['act'][0]['@id'], className=action['act'][0]['@class'],
                     text=action['act'][0]['@text'], index=action['act'][0]['@index']).exists:
            if action['act'][0]['@direction'] == 'x':
                if random.random() > 0.5:
                    db.device(resourceId=action['act'][0]['@id']).swipe("right", len)
                else:
                    db.device(resourceId=action['act'][0]['@id']).swipe("left", len)
            else:
                db.device(resourceId=action['act'][0]['@id'], className=action['act'][0]['@class'],
                          text=action['act'][0]['@text'], index=action['act'][0]['@index']).swipe("down", len)
        else:
            db.device.press("volume_down")
    elif action_type == "combination":
        for i in range(len(action['act'])):
            a = action['act'][i]
            if a['type'] == 'input':
                db.device(resourceId=a['id'], className='android.widget.EditText').send_keys(a['text'])
            elif a['type'] == 'click':
                db.device(resourceId=a['id']).click()
    else:
        print("The action is not valid.")
    time.sleep(10)


def get_view_bounds(node):
    temp = node['@bounds']
    for i in range(len(temp)):
        if temp[i] == ']':
            temp = temp[0:i + 1] + "," + temp[i + 1:]
            break
    pos = eval(temp)
    return pos


def get_view_position(action):
    act = action['act'][0]
    pos = get_view_bounds(act)
    x = (pos[0][0] + pos[1][0]) / 2
    y = (pos[0][1] + pos[1][1]) / 2
    return x, y
