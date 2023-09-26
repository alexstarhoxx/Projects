# My Liberation Notes✧˖°


## Introduction
> This was my final project of [CS50](https://cs50.harvard.edu/x/2022/), Harvard university of Introduction to the intellectual enterprises of computer science and the art of programming. This project used Python, CSS, HTML, Javascript and SQL to develop webpages.

### **Purpose of this project**
The project is inspired by a Korean drama called "[My Liberation Notes](https://tv.jtbc.joins.com/myliberationnotes)" acted by Kim Ji-won, Sukku Son, Lee Min-Ki and Lee El. (You can watch it on Netflix if you like ଘ(੭ˊᵕˋ)੭)

So what is a "Liberation" means? Well, if you feel like you are on a trap or in a difficult time and feel like you'll never get rid of these situations, you may want to have some changes and liberate yourself from that hard situation. Once you decide to begin the journey to change and liberation, note your journey, because this may not only help you remind you of being honest, but also encourage you to be confronted to difficulties bravely and let you see your progress during this journey.

You will see more details about introduction when you visit the website.


## Explaining details of my project
As you can see, this project used SQL and flask framwork to write backend code. All HTML files are on templates file and all CSS files and pictures are beneath static file.

This section will lead you see whole picture of my project and let you know main framework of this website. You are encouraged to see more details by looking through all codes that I've wrote. (͏ ˉ ꈊ ˉ)✧˖°

### **Database**
The database file is called "project.db". I created 2 tables for saving my data.
- First table users, where id, username and hash here. "id" is a primary key of this table.
- Second notes, where person_id, note_id, title, content, date, time are put here."person_id" is a foreign key which is referred to "id" in users table. "note_id" is used to distinguish notes of a user, that is to say identify a specific note of a user.


### **Templates**
The layout.html file and error.html file are designed to create log in and register webpage, while layout2.html and error2.html file are to render the main webpage after users log in.

login.html, register.html, about.html, noting.html, my-notes.html and discover.html is created to render different main webpages of this project. You can see them by clicking words on navigation section on top of the webpage.

After you have written your note, your articles will be saved on the "My Notes" page where you can find some horizontal bar buttons indicating tile, date and time of your journals. Through clicking on it, you will find out the content of your written article and you can resubmit or delete it by clicking the buttons below the webpage. Note-page.html is to render your written article and allow you to be able to resubmit or delete it.

### **Static**
Styles.css is used to render login page and register page(login.html, register.html and error.html), while body.css is to render the rest of html files.


### **helper.py**
I adopted helper.py file on Problem Set 9(Finance). 3 functions are used to help me write backend code more fluently. error and error2 functions are used to give error messages when they do not type in anything or type wrong username, password, confirmation etc. login_required function guarantees that some backend code would not be executed unless users log in.

## Presentation on Youtube
You are able to see my presentation on Youtube to show you how to use this website to write and manage your journel. Please feel free to click [here](https://youtu.be/TJ8YsNnjOWY)~~（๑ `▽´๑)

## Final Suggestions
This website is created by my own. After doing it, I do feel that it's time-consuming and difficult to do a large project (even it's not that large LOL). There will be a lot of trouble that you are going to meet with and attempt to solve on your own. However, I'm so satisfied with what I have done and feel great fulfullment when I finally finished it.

Before doing final project, I do suggest:
- Find a **classmate** to cooperate with you. When you meet difficulties or have some creative ideas, you are able to communicate with your mates and solve the problems or get a more excellent idea.
- Be patient and have a plan. Big project decides its inevitable time-consuming and hard attributes. Having a plan will help you know what to do at this moment and let you concentrate to only this **solely part** rather than whole overwhelming thing. With a clear plan, you will do things and solve problems more methodically and orderly.

This was My Liberation Notes! ꒰ ´͈ ᵕ `͈ ꒱