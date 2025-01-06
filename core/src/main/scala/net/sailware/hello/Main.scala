package net.sailware.hello

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.GL20
import java.util.Random

class Main extends ApplicationAdapter:

  var shape: ShapeRenderer = null
  var balls: scala.collection.mutable.Set[Ball] = scala.collection.mutable.Set()

  override def create(): Unit =
    shape = ShapeRenderer()
    val random = Random()
    Range(0, 10).foreach(value =>
        println(s"Creating ball: $value")
        val ball = Ball(
            random.nextInt(Gdx.graphics.getWidth()).toFloat,
            random.nextInt(Gdx.graphics.getHeight()).toFloat,
            random.nextInt(100).toFloat,
            random.nextInt(15).toFloat,
            random.nextInt(15).toFloat
        )
        balls += ball
    )
    println(s"Balls: $balls")

  override def render(): Unit =
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    shape.begin(ShapeRenderer.ShapeType.Filled)
    balls.foreach(ball =>
      ball.update()
      ball.draw(shape)
    )
    shape.end()

class Ball(
    var x: Float,
    var y: Float,
    var size: Float,
    var xSpeed: Float,
    var ySpeed: Float
):

  def update(): Unit =
    x += xSpeed
    y += ySpeed

    if x < 0 || x > Gdx.graphics.getWidth() then
      xSpeed = -xSpeed

    if y < 0 || y > Gdx.graphics.getHeight() then
      ySpeed = -ySpeed

  def draw(shape: ShapeRenderer): Unit =
    shape.circle(x, y, size)
