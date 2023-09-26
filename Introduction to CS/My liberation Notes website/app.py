import os
import time
import datetime
import random

from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import login_required, error, error2

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True

# Configure session to use filesystem(instead of signed cookies)
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///project.db")

@app.after_request
def after_request(response):
    """Ensure responses aren't cached"""
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


@app.route("/")
@login_required
def about():
    return render_template("about.html")


@app.route("/noting", methods=["POST", "GET"])
@login_required
def noting():

    if request.method == "POST":

        now = datetime.datetime.now().strftime("%Y/%m/%d %H:%M:%S")
        judge = False
        date = now[0:10]
        time = now[11:19]
        id = session.get("user_id")
        title = request.form.get("title")
        content = request.form.get("text-area")
        # If user does not enter any content
        if not title or not content:
            return error2("Blank space!", 400)

        # Put title and content into database
        db.execute("INSERT INTO notes (person_id, title, content, date, time) VALUES (?, ?, ?, ?, ?)", id, title, content, date, time)

        # If registered successfully, let judge be True so that reminder will appear
        judge = True

        return render_template("noting.html", judge=judge)

    else:
        return render_template("noting.html")


@app.route("/resubmit", methods=["POST", "GET"])
@login_required
def resubmit():

    if request.method == "POST":
        now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        date = now[0:10]
        time = now[11:19]
        id = session.get("user_id")
        title = request.form.get("title")
        content = request.form.get("text-area")
        note_id = request.form.get("note_id")

        # If user does not enter any content
        if not title or not content:
            return error2("Blank space!", 400)

        # Update the data
        db.execute("UPDATE notes SET title = ?, content = ?, date = ?, time = ? WHERE person_id = ? AND note_id = ?", title, content, date, time, id, note_id)

        return redirect("/my-notes")

    else:
        return redirect("/my-notes")


@app.route("/delete", methods=["POST", "GET"])
@login_required
def delete():

    if request.method == "POST":

        id = session.get("user_id")
        date = request.form.get("date-hidden")
        time = request.form.get("time-hidden")

        # Find out which note it is and delete the data of this note
        note_id = db.execute("SELECT note_id FROM notes WHERE person_id = ? AND date = ? AND time = ?", id, date, time)[0]["note_id"]
        db.execute("DELETE FROM notes WHERE note_id = ?", note_id)

        return redirect("/my-notes")

    else:
        return redirect("/my-notes")


@app.route("/note-page", methods=["POST", "GET"])
@login_required
def note_page():

    if request.method == "POST":

        title = request.form.get("title")
        date = request.form.get("date")
        time = request.form.get("time")
        note_id = request.form.get("note_id")

        # Get the main content by using title and date
        content = db.execute("SELECT content FROM notes WHERE title = ? AND date = ?", title, date)[0]["content"]

        return render_template("Note-page.html", title=title, content=content, date=date, time=time, note_id=note_id)

    else:
        return redirect("/my-notes")

@app.route("/my-notes")
@login_required
def mynotes():

    id = session.get("user_id")

    # Select data of all articles that user wrote
    data = db.execute("SELECT note_id, title, date, time FROM notes WHERE person_id = ?", id)

    return render_template("my-notes.html", data=data)


@app.route("/discover")
@login_required
def discover():

    id = session.get("user_id")

    # Select data of all articles that other users wrote(rather then this user)
    data = db.execute("SELECT title, content, person_id FROM notes WHERE NOT person_id = ?", id)

    # See how much articles there are, and choose a random article to demonstrate
    length = len(data)
    number = random.randint(0, length - 1)
    title = data[number]["title"]
    content = data[number]["content"]
    person_id = data[number]["person_id"]

    return render_template("discover.html", title=title, content=content, person_id=person_id)


@app.route("/login",  methods=["POST", "GET"])
def login():

    # Forget any user's id
    session.clear()

    if request.method == "POST":

        #If input form is blank, give error message
        username = request.form.get("username")
        password = request.form.get("password")

        if not username or not password:
            return error("Blank Space!", 400)

        row = db.execute("SELECT * FROM users WHERE username = ?", username)

        # If cannot find username or the inputed password is not correct, give error message
        if len(row) != 1 or not check_password_hash(row[0]["hash"], password):
            return error("Username or password Incorrect!", 400)

        # Remember the loged in user
        session["user_id"] = row[0]["id"]

        return redirect("/")

    else:
        return render_template("login.html")

@app.route("/logout")
def logout():

    # Clean session
    session.clear()

    return redirect("/login")


@app.route("/register", methods=["POST", "GET"])
def register():

    if request.method == "POST":

        # If username, password, or confirmation is blank, give error message
        username = request.form.get("username")
        password = request.form.get("password")
        confirmation = request.form.get("confirmation")
        judge = False

        if not username or not password or not confirmation:
            return error("Blank space!", 400)

        # If password does not equal to confiramtion, give error message
        if password != confirmation:
            return error("Wrong Confirmation", 400)

        # If username has had in database, give error message
        if len(db.execute("SELECT * FROM users WHERE username = ?", username)) == 1:
            return error("This username has been registered! Please use another username", 400)

        hash = generate_password_hash(password)
        db.execute("INSERT INTO users(username, hash) VALUES(?, ?)", username, hash)

        # If registered successfully, let judge be True so that reminder will appear
        judge = True

        return render_template("register.html", judge=judge)

    else:
        return render_template("register.html")