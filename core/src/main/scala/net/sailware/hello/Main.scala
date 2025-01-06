package net.sailware.hello

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import java.util.Random

class Main extends ApplicationAdapter:

  var shape: ShapeRenderer = null
  var ball: Ball = null
  var paddle: Paddle = null

  override def create(): Unit =
    shape = ShapeRenderer()
    ball = Ball(Gdx.graphics.getWidth() / 2F, 50F, 25F, 2.5F, 2.5F)
    paddle = Paddle(Gdx.graphics.getWidth() / 2F - 40F , 15F, 80F, 10F)

  override def render(): Unit =
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    paddle.update()
    ball.update(paddle)
    shape.begin(ShapeRenderer.ShapeType.Filled)
    paddle.draw(shape)
    ball.draw(shape)
    shape.end()

class Ball(
    var x: Float,
    var y: Float,
    var size: Float,
    var xSpeed: Float,
    var ySpeed: Float
):

  var color = Color.WHITE

  def update(paddle: Paddle): Unit =
    x += xSpeed
    y += ySpeed

    if x < 0 + size / 2F || x > Gdx.graphics.getWidth() - size / 2F then
      xSpeed = -xSpeed

    if y < 0 + size / 2F || y > Gdx.graphics.getHeight() - size / 2F then
      ySpeed = -ySpeed

    if collidedWithPaddle(paddle) then
      ySpeed = -ySpeed

  def draw(shape: ShapeRenderer): Unit =
    shape.setColor(color)
    shape.circle(x, y, size)

  def collidedWithPaddle(paddle: Paddle): Boolean =
    if x + size < paddle.x then false
    else if x - size > paddle.x + paddle.width then false
    else if y + size < paddle.y then false
    else if y - size > paddle.y + paddle.height then false
    else true

class Paddle(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
):

  def update(): Unit =
    x = Gdx.input.getX() - width / 2F

  def draw(shape: ShapeRenderer): Unit =
    shape.rect(x, y, width, height)
