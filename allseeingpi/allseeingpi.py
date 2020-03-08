#!/usr/bin/env python

# PiTiltHat pins 1 - 12
# pin 2 3V
# pin 3 Gnd
# pin 7 Button (-, up)
# pin 8 Button (-, down)
# pin 9 Button 10 (red button, brown wire, take picture)
# pin 10 Button 8 (blue button, blue wire, overlay)
# pin 11 Button 11 (orange wire, left)
# pin 12 Button 9 (yellow wire, right)

import paho.mqtt.client as mqtt
import pantilthat
import uuid
import threading

from picamera import PiCamera
from gpiozero import Button
from overlay_functions import *
from time import gmtime, strftime
from guizero import App, PushButton, Text, Picture
from twython import Twython
from auth import (
    consumer_key,
    consumer_secret,
    access_token,
    access_token_secret
)

# Callback for receiving CONNACK response from server
def on_connect(client, userdata, flags, rc):
	print("Subscribe to " + message_topic)
	client.subscribe(message_topic)

def on_message(client, userdata, msg):
	print(msg.topic +" "+str(msg.payload))
	if (msg.topic == message_topic):
		if ((msg.payload) == bytes(b"move_down")):
			move_down()
		if ((msg.payload) == bytes(b"move_up")):
			move_up()
		if ((msg.payload) == bytes(b"move_left")):
			move_left()
		if ((msg.payload) == bytes(b"move_right")):
			move_right()
		if ((msg.payload) == bytes(b"light_on")):
			print("Switch lights on")
			switch_lights(True)
		if ((msg.payload) == bytes(b"light_off")):
			print("Switch lights off")
			switch_lights(False)
		if ((msg.payload) == bytes(b"next_overlay")):
			next_overlay()
		if ((msg.payload) == bytes(b"take_picture")):
			take_picture()
		if ((msg.payload) == bytes(b"new_picture")):
			new_picture()
		if ((msg.payload) == bytes(b"tweet")):
			send_tweet()

# Switch neopixels on/off
def switch_lights(light_on):
    if light_on == True:
        pantilthat.set_all(255, 255, 255, 255)
    else:
	     pantilthat.set_all(0, 0, 0, 0)
    # light_on = not light_on
    pantilthat.show()

# Tell the next overlay button what to do
def next_overlay():
    global overlay
    overlay = next(all_overlays)
    preview_overlay(camera, overlay)

# Tell the take picture button what to do
def take_picture():
    global output
    output = strftime("/home/pi/allseeingpi/image-%d-%m %H:%M.png", gmtime())
    camera.capture(output)
    camera.stop_preview()

    remove_overlays(camera)
    output_overlay(output, overlay)

    # Save a smaller gif
    size = 400, 400
    gif_img = Image.open(output)
    gif_img.thumbnail(size, Image.ANTIALIAS)
    gif_img.save(latest_photo, 'gif')

    # Set the gui picture to this picture
    # your_pic.set(latest_photo)
    your_pic.value = latest_photo

def new_picture():
    camera.start_preview(alpha=128)
    preview_overlay(camera, overlay)


def send_tweet():
    twitter = Twython(
        consumer_key,
        consumer_secret,
        access_token,
        access_token_secret
    )

    # Send the tweet
    message = "The All Seeing Pi saw you!"
    with open(output, 'rb') as photo:
        twitter.update_status_with_media(status=message, media=photo)

def move_right():
	print("moved right")
	pantilthat.pan(pantilthat.get_pan()-3)
def move_left():
	pantilthat.pan(pantilthat.get_pan()+3)
	print("moved left")
def move_down():
	print("moved down")
	pantilthat.tilt(pantilthat.get_tilt()+3)
def move_up():
	print("moved up")
	pantilthat.tilt(pantilthat.get_tilt()-3)

light_on = False
pantilthat.light_mode(pantilthat.WS2812)
pantilthat.light_type(pantilthat.GRBW)
# Set up buttons
next_overlay_btn = Button(8)
next_overlay_btn.when_pressed = next_overlay
take_pic_btn = Button(10)
take_pic_btn.when_pressed = take_picture
go_left = Button(11)
go_left.when_pressed = move_left
go_right = Button(9)
go_right.when_pressed = move_right
go_up = Button(15) # 12, 18, 13
go_up.when_pressed = move_up
go_down = Button(14) # 14, 15
go_down.when_pressed = move_down

mac = hex(uuid.getnode())
message_topic = "pi/" + mac + "/camera"
print(message_topic)
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message
client.connect("10.3.141.2", 1883, 60)
thread = threading.Thread(target=client.loop_forever)
thread.setDaemon(True)
thread.start()

# Set up camera (with resolution of the touchscreen)
camera = PiCamera()
camera.rotation = 180
camera.resolution = (800, 480)
camera.hflip = True

# Start camera preview
camera.start_preview(alpha=128)

# Set up filename
output = ""

latest_photo = '/home/pi/allseeingpi/latest.gif'

app = App("The All Seeing Pi", 800, 480)
#app.attributes("-fullscreen", True)
message = Text(app, "I spotted you!")
your_pic = Picture(app, latest_photo)
new_pic = PushButton(app, new_picture, text="New picture")
tweet_pic = PushButton(app, send_tweet, text="Tweet picture")
take_pic = PushButton(app, take_picture, text="Take picture")
next_over = PushButton(app, next_overlay, text="Next overlay")
#lights_on = PushButton(app, switch_lights(light_on), text="Lights on/off")

app.display()
print(dir(camera))
