def chat_315811047(player):
    player.npcChat("Greetings outlander!")
    player.nextChat(315811048)

def chat_315811048(player):
    player.dialogueOption("What is this place?", 315811049, "Do you have any quests?", 315811053)
    
def chat_315811049(player):
    player.playerChat("What is this place?")
    player.nextChat(315811050)

def chat_315811050(player):
    player.npcChat("This place? Why this is Relekka! Homeland of all", "Fremennik! I do not recognise your face outlander;", "Where do you come from?")
    player.nextChat(315811051)

def chat_315811051(player):
    player.playerChat("That's not important...")
    player.nextChat(315811052)

def chat_315811052(player):
    player.npcChat("Hmmm... I will not press the issue then outlander.", "How may my tribe and I help you?")
    player.nextChat(315811053)

def chat_315811053(player):
    player.playerChat("Do you have any quests?")
    player.nextChat(315811054)

def chat_315811054(player):
    player.npcChat("Quests you say outlander? Well, I would not call it a", "quest as such, but if you are brave of heart and strong", "of body, perhaps...")
    player.nextChat(315811055)

def chat_315811055(player):
    player.npcChat("No, you would not be interested. Forget I said", "anything, outerlander.")
    player.nextChat(315811056)

def chat_315811056(player):
    player.dialogueOption("Yes, I am interested.", 315811059, "No, I'm not interested.", 315811057)

def chat_315811057(player):
    player.playerChat("No, I'm not interested.")
    player.nextChat(315811058)

def chat_315811058(player):
    player.npcChat("As I thought.")
    player.endChat()

def chat_315811059(player):
    player.playerChat("Actually, I would be very interested to hear what you", "have to offer.")
    player.nextChat(315811060)

def chat_315811060(player):
    player.npcChat("You would? These are unusual sentiments to hear from", "an outerlander! My suggestion was going to be that if", "you crave adventure and battle,")
    player.nextChat(315811061)

def chat_315811061(player):
    player.npcChat("and your heart sings for glory, thn perhaps you would", "be interested in joining our clan, and becoming a", "Fremennik yourself?")
    player.nextChat(315811062)

def chat_315811062(player):
    player.playerChat("What would that involve exactly?")
    player.nextChat(315811063)

def chat_315811063(player):
    player.npcChat("Well, there are two ways to become a member of our", "clan and call yourself a Fremennik: be born a", "Fremennik, or be voted in by our council of elders.")
    player.nextChat(315811064)

def chat_315811064(player):
    player.playerChat("Well, I think I've missed the first way, but how can I", "get the council of elders to vote to let me join your", "clan")
    player.nextChat(315811065)

def chat_315811065(player):
    player.npcChat("Well, that I cannot answer myself. You will need to", "speak to each of them, and see what they require of you", "as proof of your dedication")
    player.nextChat(315811066)

def chat_315811066(player):
    player.npcChat("There are twelve council members around this village;", "you wil need to gain a majority vote of at least five", "councillors in your favour.")
    player.nextChat(315811067)

def chat_315811067(player):
    player.npcChat("So what say you? Give me the world, and I will tell all","of my tribe of your intentions, be they yea or nay.")
    player.nextChat(315811068)

def chat_315811068(player):
    player.dialogueOption("I want to become a Fremennik!", 315811070, "I don't want to become a Fremennik.", 315811069)
    
def chat_315811069(player):
    player.playerChat("I don't want to become a stinky wanna-be viking.")
    player.endChat()

def chat_315811070(player):
    player.playerChat("I think I would njoy the challenge of becoming an", "honorary Freminek. Where and how do I start?")
    player.nextChat(315811071)

def chat_315811071(player):
    player.npcChat("As I say outerlander, you must find and speak to the", "eight members of the council of elders and see what", "tasks they might set you.")
    player.nextChat(315811072)

def chat_315811072(player):
    player.npcChat("If you gain the support of 5 of the twelve, then", "you will be accepted as one of us without question.")
    player.nextChat(315811073)