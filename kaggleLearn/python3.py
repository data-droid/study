#Chapter6 ; Strings and Dictionaries
x = 'Pluto is a planet'
y = "Pluto is a planet"
print(x==y)
print('Pluto\'s a planet!',"Pluto's a planet!")
hello = "hello\nworld"
triplequoted_hello = """hello
world"""
print(triplequoted_hello)
print(hello==triplequoted_hello)

planet = 'Pluto'
print(planet[0], planet[-3:], len(planet))
print([char+'!' for char in planet])
#planet[0] = 'B' # can't modify

claim = "Pluto is a planet!"
print(claim.upper(),claim.lower())
print(claim.index('plan')) # 11
print(claim.startswith(planet)) # Pluto로 시작하는가?
print(claim.endswith('planet!')) # planet로 끝나는가?

words = claim.split()
print(words)

datestr = '1956-01-31'
year, month, day = datestr.split('-')
print(year, month, day)
print('/'.join(datestr.split('-')))
print('/'.join([year, month, day]))

print(' 👏 '.join([word.upper() for word in words]))
print(planet + ', we miss you')

position = 9
print(planet + ", you'll always be the " + str(position) + "th planet to me") # int to string 

print("{}, you'll always be the {}th planet to me.".format(planet,position)) # format string. do not need int to string

pluto_mass = 1.303 * 10**22
earth_mass = 5.9722 * 10**24
population = 52910390
print("{} weights about {:.2} kilograms ({:.3%} of Earth's mass). It's home to {:,} Plutonians.".format(planet, pluto_mass, pluto_mass/earth_mass,population))

s="""Pluto's a {0}.
No, it's a {1}.
{0}!
{1}!""".format('planet','dwarf planet')
print(s)

#Dictionaries 
numbers = {'one':1, 'two':2, 'three':3}
print(numbers['one'])
numbers['eleven'] = 11
print(numbers)
numbers['one'] = 'Pluto' # multi type OK!
print(numbers)

planets = ['Mercury', 'Venus', 'Earth', 'Mars', 'Jupiter', 'Saturn', 'Uranus', 'Neptune']
planet_to_initial = {planet: planet[0] for planet in planets}
print(planet_to_initial)

print('Saturn' in planet_to_initial)
print('Betelgeuse' in planet_to_initial)

for k in numbers :
	print("{} = {}".format(k, numbers[k]))

print(' '.join(sorted(planet_to_initial.values()))) # dict.keys(), dict.values()
print(' '.join(sorted(planet_to_initial.keys())))

for planet, initial in planet_to_initial.items() :
	print("{} = {}".format(planet.rjust(10), initial)) # rjust 우측정렬

# Chapter 7 : Working with External Libs

import math
print(dir(math))
print("pit to 4 significant digits = {:.4}".format(math.pi))
print(math.log(32,2))

import math as mt 
print(mt.pi)

from math import *
print(pi, log(32,2))

from math import *
from numpy import * 
#print(pi, log(32,2)) #TypeError: return arrays must be of ArrayType
from math import log, pi
from numpy import asarray

import numpy
print(dir(numpy.random))

rolls = numpy.random.randint(low=1, high=6, size=10)
print(rolls)
print(type(rolls))
print(dir(rolls))
print(rolls.mean(), rolls.tolist())
print(help(rolls.ravel))

#print([3,4,1,2,2,1]+10) # TypeError: can only concatenate list (not "int") to list
print(rolls, rolls+10, rolls <=3) # numpy는 되지롱!

xlist=[[1,2,3],[2,4,6],]
x = numpy.asarray(xlist)
print("xlist = {}\nx =\n{}".format(xlist,x)) 

print(x[1,-1]) # 6
# print(xlist[1,-1]) # TypeError: list indices must be integers or slices, not tuple

