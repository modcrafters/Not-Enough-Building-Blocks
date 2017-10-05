package net.modcrafters.nebb.blocks.slopes

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraftforge.client.event.DrawBlockHighlightEvent
import net.minecraftforge.common.model.TRSRTransformation
import net.modcrafters.nebb.blocks.BaseFlippableBlock
import net.modcrafters.nebb.blocks.BaseHorizontalBlock
import net.modcrafters.nebb.blocks.BaseTile
import net.modcrafters.nebb.getSprite
import net.modcrafters.nebb.parts.BigAABB
import net.modcrafters.nebb.parts.BlockInfo
import net.modcrafters.nebb.parts.PartInfo
import net.modcrafters.nebb.parts.PartTextureInfo
import net.ndrei.teslacorelib.render.selfrendering.RawLump

class TileSimpleSlope : BaseTile() {
    override fun createBlockInfo(): BlockInfo {
        return TileSimpleSlope.getModel(mapOf(), this)
    }

    companion object {
        const val PART_SLOPE = "slope"

        fun getModel(stack: ItemStack): BlockInfo {
            return TileSimpleSlope.getModel(mapOf())
        }

        private fun getModel(textureMap: Map<String, IBlockState>, tile: TileSimpleSlope? = null): BlockInfo {
            val builder = BlockInfo.getBuilder()

            val orientation = tile?.world?.getBlockState(tile.pos)?.getValue(BaseHorizontalBlock.FACING) ?: EnumFacing.NORTH
            val flip = tile?.world?.getBlockState(tile.pos)?.getValue(BaseFlippableBlock.FLIP_UP_DOWN) ?: false

            builder.add(object: PartInfo(PART_SLOPE,
                BigAABB(0.0, 0.0, 0.0, BigAABB.SCALE, BigAABB.SCALE, BigAABB.SCALE)) {

                override fun bakePartQuads(texture: PartTextureInfo, quads: MutableList<BakedQuad>, partBlockModel: IBakedModel, vertexFormat: VertexFormat, transform: TRSRTransformation) {
                    val lump = RawLump()

                    val left = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X)
                    lump.addFace(
                        arrayOf(
                            Vec3d(0.0, 0.0, 0.0),
                            Vec3d(0.0, 0.0, 32.0),
                            Vec3d(0.0, 32.0, 32.0)
                        ), arrayOf(
                            Vec2f(0.0f, 16.0f),
                            Vec2f(16.0f, 16.0f),
                            Vec2f(16.0f, 0.0f)
                        ),
                        partBlockModel.getSprite(texture.block, left), left
                    )

                    val right = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X)
                    lump.addFace(
                        arrayOf(
                            Vec3d(32.0, 0.0, 0.0),
                            Vec3d(32.0, 32.0, 32.0),
                            Vec3d(32.0, 0.0, 32.0)
                        ), arrayOf(
                            Vec2f(16.0f, 16.0f),
                            Vec2f(0.0f, 0.0f),
                            Vec2f(0.0f, 16.0f)
                        ),
                        partBlockModel.getSprite(texture.block, right), right
                    )

                    val back = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z)
                    lump.addFace(
                        arrayOf(
                            Vec3d(32.0, 0.0, 32.0),
                            Vec3d(32.0, 32.0, 32.0),
                            Vec3d(0.0, 32.0, 32.0),
                            Vec3d(0.0, 0.0, 32.0)
                        ), arrayOf(
                        Vec2f(0.0f, 16.0f),
                        Vec2f(0.0f, 0.0f),
                        Vec2f(16.0f, 0.0f),
                        Vec2f(16.0f, 16.0f)
                    ),
                        partBlockModel.getSprite(texture.block, back), back
                    )

                    val front = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z)
                    lump.addFace(
                        arrayOf(
                            Vec3d(0.0, 0.0, 0.0),
                            Vec3d(0.0, 32.0, 32.0),
                            Vec3d(32.0, 32.0, 32.0),
                            Vec3d(32.0, 0.0, 0.0)
                        ), arrayOf(
                        Vec2f(0.0f, 16.0f),
                        Vec2f(0.0f, 0.0f),
                        Vec2f(16.0f, 0.0f),
                        Vec2f(16.0f, 16.0f)
                    ),
                        partBlockModel.getSprite(texture.block, front), front
                    )

                    val bottom = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y)
                    lump.addFace(
                        arrayOf(
                            Vec3d(0.0, 0.0, 0.0),
                            Vec3d(32.0, 0.0, 0.0),
                            Vec3d(32.0, 0.0, 32.0),
                            Vec3d(0.0, 0.0, 32.0)
                        ), arrayOf(
                        Vec2f(0.0f, 16.0f),
                        Vec2f(0.0f, 0.0f),
                        Vec2f(16.0f, 0.0f),
                        Vec2f(16.0f, 16.0f)
                    ),
                        partBlockModel.getSprite(texture.block, bottom), bottom
                    )

                    lump.bake(quads, vertexFormat, transform)
                }

                override fun renderOutline(ev: DrawBlockHighlightEvent) {
                    val tessellator = Tessellator.getInstance()
                    val buffer = tessellator.buffer
                    buffer.begin(3, DefaultVertexFormats.POSITION_COLOR)

                    buffer.pos(0.0, 0.0, 0.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(0.0, 0.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(0.0, 1.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 1.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 0.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 0.0, 0.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(0.0, 0.0, 0.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(0.0, 1.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(0.0, 0.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 0.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 0.0, 0.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()
                    buffer.pos(1.0, 1.0, 1.0).color(0.0f, 1.0f, 0.0f, 0.4f).endVertex()

                    val dx = ev.player.lastTickPosX + (ev.player.posX - ev.player.lastTickPosX) * ev.partialTicks.toDouble()
                    val dy = ev.player.lastTickPosY + (ev.player.posY - ev.player.lastTickPosY) * ev.partialTicks.toDouble()
                    val dz = ev.player.lastTickPosZ + (ev.player.posZ - ev.player.lastTickPosZ) * ev.partialTicks.toDouble()
                    val offset = Vec3d(dx, dy, dz).subtractReverse(Vec3d(ev.target.blockPos.x.toDouble(), ev.target.blockPos.y.toDouble(), ev.target.blockPos.z.toDouble()))

                    GlStateManager.pushMatrix()
                    GlStateManager.translate(offset.x + 0.5, offset.y + 0.5, offset.z + 0.5)
                    GlStateManager.rotate(when (orientation) {
                        EnumFacing.WEST -> 90.0f
                        EnumFacing.SOUTH -> 180.0f
                        EnumFacing.EAST -> 270.0f
                        else -> 0.0f
                    }, 0.0f, 1.0f, 0.0f)
                    if (flip) {
                        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f)
                    }
                    GlStateManager.translate(-0.5, -0.5, -.5)
                    tessellator.draw()
                    GlStateManager.popMatrix()
                }
            },
                textureMap.getOrDefault(PART_SLOPE, Blocks.PLANKS.defaultState))

            builder.setCacheKeyTransformer { "slope_simple::${orientation.name}$it" }

            return builder.build()
        }
    }
}
