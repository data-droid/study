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

