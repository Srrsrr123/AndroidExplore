import uiautomator2 as u2


# com.example.asd                          (budgewatch)
# com.hui.tally                            (tally)
# com.example.notes                        (simplenotes)
# com.demo.appmonitor                      (monitor)
# com.zhihaofans.texttext                  (text)
# com.xinyang.calendarview                 (calendar)
# com.northenbank.pomodoro                 (pomodoro)
# com.example.lrp.auto                     (irp)
# net.thebrennt.anycut                     (anycut)
# com.example.weather                      (weather)
# top.ysccx.myfirstapp                     (llk)
# com.talengu.wordwarrior                  (word)
# me.veryyoung.game2048                    (2048)
# com.example.rubbishrecog                 (rubbish)
# comjoshsibayan.github.alarm              (alarm)
# news.androidtv.quicksettingstv           (quicksetting)
# com.example.vasilis.myapplication        (autoanswer)
# in.shick.lockpatterngenerator            (lock)
# de.meonwax.soundboard                    (soundboard)
# com.example.helloworld                   (smss)
# com.jeremy.passwordmaker                 (passwordmaker)
# com.example.ben.tezfillup                (fillup)
# be.ppareit.swiftp                        (swiftp)
# com.hectorone.multismssender             (multismssender)
# edu.utep.cs.cs4330.dumbphone             (dumbphone)
# com.agmcs.countdown                      (countday)
# com.example.android.divideandconquer     (divideandconquer)
# org.jtb.alogcat                          (alogcat)
# org.secuso.privacyfriendlysketching      (sketch)
# com.android.keepass                      (keepass)

AUT = "com.hui.tally"

device = u2.connect()
sim_state_id = ''
sim_state = ''
start_state = ''
similarity = -100
reward = 0
T_abu = {}
current_event = []
events = {}
episode_max_step = 100
DFA = {}
index = ''
DFA_max_step = 10
N = {}
