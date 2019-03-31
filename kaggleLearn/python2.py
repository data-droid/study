#Chapter3 : Booleans and Conditionals
x=True
print(x)
print(type(x))

def can_run_for_president(age) :
	""" Can someone of the given age run for president in the US?"""
	return age >= 35

print("Can a 19-year-old run for president?", can_run_for_president(19))	
print("Can a 45-year-old run for president?", can_run_for_president(45))

print(3.0 == 3)
print('3'==3) #false 

def is_odd(n) :
	return (n%2)==1
print("100?",is_odd(100))
print("-1?",is_odd(-1))

def can_run_for_president(age, is_natural_born_citizen) :
	"""Can someone of the given age, citizenship status run for president in the US?"""
	return is_natural_born_citizen and (age >= 35)

print(can_run_for_president(19,True))
print(can_run_for_president(55,False))
print(can_run_for_president(55,True))
print(True or True and False) # True

def inspect(x) :
	if x==0 :
		print(x, "is zero")
	elif x>0 :
		print(x, "is positive")
	elif x<0 :
		print(x, "is negatvie")
	else :
		print(x, "is unlike anything I've ever seen")

inspect(0)
inspect(-15)

def f(x) :
	if x>0 :
		print("Only printed when x is positive; x = ",x)
		print("Also only printed when x is positive; x = ",x)
	print("Always printed, regardless of x's value; x = ",x)
f(1)
f(0)

print(bool(1)) #true
print(bool(0)) #false
print(bool("asd")) #true
print(bool("")) #false

if 0 :
	print(0)
elif "spam" :
	print("spam") #print

def quiz_message(grade) :
	if grade < 50:
		outcome = 'failed'
	else :
		outcome = 'passed'
	print('You', outcome, 'the quiz with a grade of',grade)

quiz_message(80)

def quiz_message(grade) :
	outcome = 'failed' if grade < 50 else 'passed'
	print('You', outcome, 'the quiz with a grade of',grade)

quiz_message(40)