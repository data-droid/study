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
	outcome = 'failed' if grade < 50 else 'passed' # 특이함!
	print('You', outcome, 'the quiz with a grade of',grade)

quiz_message(40)


#Chapter 4 : LIST
primes = [2,3,5,7]
print(primes)
planets = ['Mercury', 'Venus', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune']
print(planets)
hands = [['J', 'Q', 'K'], ['2', '2', '2'], ['6', 'A', 'K']] # list of list
print(hands)
my_favorite_things = [32,'raindrops on roses',help] #mixed type
print(my_favorite_things)

print(planets[1]) #indexing from 0 >>Venus
print(planets[-1]) #indexing Negative from back >> Neptune

print(planets[1:3]) #Slicing 1<=i<3
print(planets[:3]) #Slicing i<3
print(planets[3:]) #Slicing i>=3

print(planets[1:-1]) #Slicing 1<=i<last
print(planets[-3:]) #Slicing last-3<=i

planets[3] = "Malacandra" 
print(planets)

planets[:3] = ['Mur', 'Vee', 'Ur']
print(planets)

planets[:4] = ['Mercury', 'Venus', 'Earth', 'Mars']
print(planets)

print(len(planets)) #List length
print(sorted(planets)) #dic sort

print(primes, sum(primes)) # list sum
print(primes, max(primes)) # list max

##
x = 12
print(x.imag) # x is a real number, imaginary part is 0.
c = 12 + 3j
print(c.imag) # 3.0
print(x.bit_length()) # bit변환시 길이
##

planets.append('Pluto') # append
print(planets)

print(planets.pop()) # pop
print(planets)
print(planets.index('Earth')) # search 
#print(planets.index('Pluto')) # error!
print("Earth" in planets) #True
print("Calefraques" in planets) #False
#help(planets) 

#Tuples
t = (1,2,3)
print(t)
t = 1, 2, 3
print(t)
# t[0] = 100 # error! cannot be modified
x = 0.125
print(x.as_integer_ratio()) # 분자/분모 값 반환 
numerator, denominator = x.as_integer_ratio()
print(numerator,denominator, numerator/denominator)

#Chapter 5 : Loops
planets = ['Mercury', 'Venus', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune']
for planet in planets :
	print(planet, end= ' ') # default end='\n'

multiplicands = (2,2,2,3,3,5)
product =1
for mult in multiplicands :
	product *= mult
print(product)

s = 'steganograpHy is the practicE of conceaLing a file, message, image, or video within another fiLe, message, image, Or video.'
msg = ''
for char in s :
	if char.isupper() :
		print(char, end='')

for i in range(5) :
	print("i =",i)

i=0
while i<10 :
	print(i, end=' ')
	i+=1

squares = [n**2 for n in range(10)]
print(squares)

squares = []
for n in range(10) :
	squares.append(n**2)
print(squares)

short_planets = [planet for planet in planets if len(planet) < 6]
print(short_planets)

loud_short_planets = [planet.upper() + '!' for planet in planets if len(planet) < 6]
print(loud_short_planets)

print([32 for planet in planets]) # [32, 32, ... , 32]

def count_negatives(nums) :
	n_negative = 0
	for num in nums :
		if num <0 :
			n_negative++
	return n_negative

def count_negatives(nums) :
	return len([num for num in nums if nums<0])

def count_negatives(nums) :
	return sum([num < 0 for num in nums])