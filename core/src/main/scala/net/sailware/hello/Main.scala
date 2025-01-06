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
  var bricks: Array[Brick] = null

  override def create(): Unit =
    shape = ShapeRenderer()
    ball = Ball(Gdx.graphics.getWidth() / 2F, 50F, 10F, 2.5F, 2.5F)
    paddle = Paddle(Gdx.graphics.getWidth() / 2F - 40F , 15F, 80F, 10F)
    bricks = Range(0, 4).map(row =>
        Range(0, 8).map(column =>
            Brick(
                (column * 63F) + (column * 20F),
                Gdx.graphics.getHeight() - (row * 40F),
                63F,
                20F
            )
        )
    ).flatten.toArray

  override def render(): Unit =
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

    paddle.update()
    ball.update()

    val destroyedBricks = bricks.filter(brick => Collision.ballWithBrick(ball, brick))
    bricks = bricks.diff(destroyedBricks)

    if Collision.ballWithPaddle(ball, paddle) || destroyedBricks.nonEmpty then
      ball.collision()

    shape.begin(ShapeRenderer.ShapeType.Filled)
    paddle.draw(shape)
    ball.draw(shape)
    bricks.foreach(brick => brick.draw(shape))
    shape.end()

class Ball(
    var x: Float,
    var y: Float,
    var size: Float,
    var xSpeed: Float,
    var ySpeed: Float
):

  var color = Color.WHITE

  def update(): Unit =
    x += xSpeed
    y += ySpeed

    if x < 0 + size / 2F || x > Gdx.graphics.getWidth() - size / 2F then
      xSpeed = -xSpeed

    if y < 0 + size / 2F || y > Gdx.graphics.getHeight() - size / 2F then
      ySpeed = -ySpeed

  def collision(): Unit =
    ySpeed = -ySpeed

  def draw(shape: ShapeRenderer): Unit =
    shape.setColor(color)
    shape.circle(x, y, size)

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

class Brick(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
):

  def draw(shape: ShapeRenderer): Unit =
    shape.rect(x, y, width, height)

object Collision:
  def ballWithPaddle(ball: Ball, paddle: Paddle): Boolean =
    if ball.x + ball.size < paddle.x then false
    else if ball.x - ball.size > paddle.x + paddle.width then false
    else if ball.y + ball.size < paddle.y then false
    else if ball.y - ball.size > paddle.y + paddle.height then false
    else true

  def ballWithBrick(ball: Ball, brick: Brick): Boolean =
    if ball.x + ball.size < brick.x then false
    else if ball.x - ball.size > brick.x + brick.width then false
    else if ball.y + ball.size < brick.y then false
    else if ball.y - ball.size > brick.y + brick.height then false
    else true
