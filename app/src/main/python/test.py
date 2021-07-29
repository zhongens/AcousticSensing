import math
import random
import numpy

def get_random():
    return random.randint(1, 100)


def get_sqrt(x):
    return math.sqrt(x)


def calculator(x,y,ope):
    print("enter python")
    if ope == "+":
        return x + y
    elif ope == "-":
        return x - y
    elif ope == "*":
        return x * y
    elif ope == "/":
        return x / y