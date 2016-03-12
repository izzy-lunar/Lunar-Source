def configure_quest_24():
    quest_id = 25
    quest_name = "Druidic Ritual"
    quest_stages = 4
    World.addQuest(quest_id, quest_name, quest_stages)

def quest_button_25(player):
    quest_stage = player.getQuest(25).getStage()
    if quest_stage == 0:
        player.getFunction().startInfo("@blu@Druidic Ritual@bla@", "I can start this quest by talking to", "@dre@Kaqemeex@bla@ who is at the @dre@Druidic circle", "just @dre@North@bla@ of @dre@Taverly@bla@.","")
    elif quest_stage == 1:
        player.boxMessage("Kaqemeex has instructed me to find Sanfew in Taverly.")
    elif quest_stage == 2:
        player.boxMessage("Sanfew has requested I bring him the enchanted meats.")
    elif quest_stage == 3:
        player.boxMessage("You should return to Kaqemeex to complete the quest.")
    elif quest_stage == 4:
        player.boxMessage("I have completed @dre@Druidic Ritual@bla@.")
 
 
 def chat_939890300(player):
    player.playerChat("Hello there.")
    player.nextChat(939890301)

def chat_939890301(player):
    player.npcChat("What brings you to our holy monument?")
    player.nextChat(939890302)

def chat_939890302(player):
    player.playerChat("I'm in search of a quest.")
    player.nextChat(939890303)

def chat_939890303(player):
    player.npcChat("Hmm. I think I may have a worthwhile quest for you", "actually. I don't know if you are familiar with the stone", "circle south of Varrock or not, but...")
    player.nextChat(939890304)

def chat_939890304(player):
    player.npcChat("That used to be OUR stone circle. Unfortunately,", "many, many years ago, dark wizards cast a wicked spell", "upon it so that they could corrupt its power for their", "own evil ends.")
    player.nextChat(939890305)

def chat_939890305(player):
    player.npcChat("When they cursed the rocks for their rituals they made", "them useless to us and our magics. We require a brave", "adventurer to gi in a quest for us to help purify the", "city of Varrock.")
    player.nextChat(939890306)

def chat_939890306(player):
    player.playerChat("Ok, I will try and help.")
    player.nextChat(939890307)

def chat_939890307(player):
    player.npcChat("Excellent. Go to the village south of this place and speak", "to my fellow Sanfew who is working on the purification", "ritual. He knows better than I what is required to", "complete it.")
    player.nextChat(939890308)

def chat_939890308(player):
    player.npcChat("Will do.")
    player.endChat()

def chat_1085684074(player):
    player.npcChat("What can I do for you young 'un?")
    player.nextChat(1085684075)

def chat_1085684075(player):
    player.playerChat("I've been sent to assist you with the ritual to purify the", "Varrockian stone circle.")
    player.nextChat(1085684076)

def chat_1085684076(player):
    player.npcChat("Well, what I'm struggling with right now is the meats", "needed for the potion to honour Guthix. I need the raw", "meat of four different animals for it, but not just any", "old meats will do.")
    player.nextChat(1085684077)

def chat_1085684077(player):
    player.npcChat("Each meat has to be dipped individually into the", "Cauldren of Thunder for it to work correctly.")
    player.nextChat(1085684078)

def chat_1085684078(player):
    player.playerChat("Where can I find this cauldron?")
    player.nextChat(1085684079)

def chat_1085684079(player):
    player.npcChat("It is located somewhere in the mysterious underground", "halls which are located somewhere in the woods just", "South of here. They are too dangerous for me to go", "myself however.")
    player.endChat()
	
	
def chat_1543042786(player):
    player.npcChat("Did you bring me the required ingredients for the", "potion?")
    player.nextChat(1543042787)

def chat_1543042787(player):
    player.playerChat("I'm still working on it.")
    player.endChat()

def chat_1543042788(player):
    player.playerChat("Yes, I have all four now!")
    player.nextChat(1543042789)

def chat_1543042789(player):
    player.npcChat("Well hand 'em over then LAD!")
    player.nextChat(1543042790)

def chat_1543042790(player):
    player.npcChat("Thank you so much adventurer! These meats will allow", "our potion to honour Guthix to be completed, and bring", "one step closer to reclaiming our stone circle!")
    player.nextChat(1543042791)

def chat_1543042791(player):
    player.npcChat("Now go and talk to Kaqemeex and he will introduce", "you to the wonderful world of herblore and potion", "making!")
    player.endChat()
	
	def chat_1543042786(player):
    player.npcChat("Did you bring me the required ingredients for the", "potion?")
    player.nextChat(1543042787)

def chat_1543042787(player):
    player.playerChat("I'm still working on it.")
    player.endChat()

def chat_1543042788(player):
    player.playerChat("Yes, I have all four now!")
    player.nextChat(1543042789)

def chat_1543042789(player):
    player.npcChat("Well hand 'em over then LAD!")
    player.nextChat(1543042790)

def chat_1543042790(player):
    player.npcChat("Thank you so much adventurer! These meats will allow", "our potion to honour Guthix to be completed, and bring", "one step closer to reclaiming our stone circle!")
    player.nextChat(1543042791)

def chat_1543042791(player):
    player.npcChat("Now go and talk to Kaqemeex and he will introduce", "you to the wonderful world of herblore and potion", "making!")
    player.endChat()

def chat_1543042792(player):
    player.playerChat("Hello there.")
    player.nextChat(1543042793)

def chat_1543042793(player):
    player.npcChat("I have word from Sanfew that you have been very", "helpful in assisting him with his preparations for the", "purification ritual. As promised I will now teach you the", "ancient arts of Herblore.")
    player.nextChat(1543042794)

def chat_1543042794(player):
    player.getQuest(25).setStage(4)
    player.refreshQuestTab()
    player.addPoints(50)
    reward = QuestReward("5,000,000 coins", "50 OXP", "1 Quest Point", "Unlocked Merlin's Taverly", "dungeon teleport")
    player.completeQuest("Druidic Ritual", reward, 199)
