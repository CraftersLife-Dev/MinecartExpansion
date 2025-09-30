/*
 * MinecartExpansion
 *
 * Copyright (c) 2025. すだち
 *                     Contributors [Namiu (うにたろう)]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.github.crafterslife.dev.minecartexpansion.utilities;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * {@link org.bukkit.util.Vector} の各成分（X, Y, Z）に対して、それぞれ数式を適用するためのレコードクラスです。
 * <p>
 * 式の中では、元のベクトルの成分が変数 {@code x}, {@code y}, {@code z} として参照可能です。
 *
 * @param xExpression X成分に対する計算式
 * @param yExpression Y成分に対する計算式
 * @param zExpression Z成分に対する計算式
 */
@NullMarked
@ConfigSerializable
public record VectorExpression(Expression xExpression, Expression yExpression, Expression zExpression) {

    /**
     * 指定された入力ベクトルに基づいて、新しいベクトルを計算し評価します。
     *
     * <p>入力ベクトルの成分は、各式の評価中に変数 {@code x}, {@code y}, {@code z} として渡されます。</p>
     *
     * @param vector 評価のための入力として使用される元の {@link Vector}
     * @return 計算された新しい {@link Vector}
     * @throws EvaluationException 式の評価中にエラーが発生した場合
     * @throws ParseException 式のパース中にエラーが発生した場合
     */
    public Vector evaluate(final Vector vector) throws EvaluationException, ParseException {
        final EvaluationValue resultX = this.applyVariable(this.xExpression, vector).evaluate();
        final EvaluationValue resultY = this.applyVariable(this.yExpression, vector).evaluate();
        final EvaluationValue resultZ = this.applyVariable(this.zExpression, vector).evaluate();

        return new Vector(
                resultX.getNumberValue().doubleValue(),
                resultY.getNumberValue().doubleValue(),
                resultZ.getNumberValue().doubleValue()
        );
    }

    /**
     * 式に、入力ベクトルの成分を変数としてバインドします。
     *
     * @param expression 変数を適用する {@link Expression}
     * @param vector 入力となる {@link Vector}
     * @return 変数がバインドされた新しい {@link Expression}
     */
    private Expression applyVariable(final Expression expression, final Vector vector) {
        final double variableX = vector.getX();
        final double variableY = vector.getY();
        final double variableZ = vector.getZ();

        return expression
                .with("x", variableX)
                .and("y", variableY)
                .and("z", variableZ);
    }
}
