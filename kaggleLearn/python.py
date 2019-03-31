# Chapter 1
spam_amount = 0
print(spam_amount)

spam_amount += 4

if spam_amount > 0 :
	print("But I don't want Any spam!")

viking_song = "spam " * spam_amount # spam spam spam spam
#viking_song = "spam " + spam_amount
print(viking_song)

print(spam_amount*4) # 16
print(float(str(spam_amount)*4)*4) #17776.0 

print(type(spam_amount)) 

print(5/2) # 2.5
print(5//2) # 2
print(5%2) # 1
print(5**2) # 25

print(min(1,2,3,4,5)) #1
print(max(1,2,3,4,5)) #5

print(abs(-32)) #32

a = [1,2,3]
b = [3,2,1]

temp=a; a=b; b=temp
print(a)

a,b = b,a #미쳤다
print(a)

#Chapter 2 : help & function
help(round)
help(round(-2.01)) #help(int)
help(print)

def least_difference(a, b, c) :
	""" Return the smallest diferrence between any two numbers
	among a, b, and c.

	>>> least_difference(1,5,-5)
	4
	"""
	diff1 = abs(a-b);
	diff2 = abs(b-c);
	diff3 = abs(a-c);
	return min(diff1, diff2, diff3)

print(
	least_difference(1,10,100),
	least_difference(1,10,10),
	least_difference(5,6,7),
	)

help(least_difference)

print(1,2,3, sep=' < ') # default sep = ' '

def greet(who="Colin") :
	print("Hello, ", who)

greet() #default who="Colin"
greet(who="Kaggle")
greet("world")

def multy_by_five(x) :
	return 5 * x

def call(fn, arg) :
	"""Call fn on arg"""
	return fn(arg)

def squared_call(fn,arg) :
	return fn(fn(arg))

print (
	call(multy_by_five, 1),
	squared_call(multy_by_five, 1),
	sep='\n',
)

def mod_5(x) :
	"""Return the remainder of x after dividing by 5"""
	return x % 5

print (
	max(100,51,14), # 100
	max(100,51,14, key=mod_5), # 각 val의 mod_5한 값 중 max인 val 14
	sep='\n',
)

print(round(511123,-2)) # 100미만 절사

from time import time
t=time()
print(t,"seconds since the Epoch")

from time import sleep
duration =5
print("Getting sleepy. See you in",duration, "seconds")
sleep(duration)
print("I'm back")