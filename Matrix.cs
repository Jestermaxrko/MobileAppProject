using System;

namespace lab1
{
    class Matrix
    {
        private int row, col;
        private int[,] M;

        public Matrix(int row, int col, int[,] M)
        {
            this.row = row;
            this.col = col;
            this.M = M;
        }
        public Matrix(int row, int col)
        {
            this.row = row;
            this.col = col;
            M = new int[row, col];
        }
        public Matrix Minor(int r, int c)
        {
            Matrix Minor_ = new Matrix(row - 1, col - 1);
            int minor_col = 0, minor_row = 0;
            for (int i = 0; i < this.row; i++, minor_col = 0)
                if (i == r)
                    continue;
                else
                {
                    for (int j = 0; j < this.col; j++)
                    {
                        if (j == c)
                            continue;
                        Minor_.M[minor_row, minor_col++] = this.M[i, j];
                    }
                    minor_row++;
                }
            return Minor_;
        }
        public Matrix Converse()
        {
            int det = this.Det();
            Matrix Conv = new Matrix(this.row, this.col);
            for (int i = 0; i < this.row; i++)
                for (int j = 0; j < this.col; j++)
                {
                    Matrix tmp = this.Minor(i, j);
                    Conv.M[j, i] = (((i + j) % 2) == 1) ? -1 : 1;
                    Conv.M[j, i] *= tmp.Det() / det;
                }
            return Conv;
        }
        public static Matrix operator /(Matrix x1, Matrix x2)
        {
            if (x1.col != x2.col || x1.row != x2.row)
            {
                throw new ArgumentException("Matrix don`t match");
            }
            Matrix d = new Matrix(x1.row, x2.col);
            for (int i = 0; i < x1.row; i++)
                for (int j = 0; j < x1.col; j++)
                    d.M[i, j] = (x1.M[i,j] != 0 && x2.M[i,j] == 0)?1:0;
            return d;
        }

        public static Matrix operator ~(Matrix t)
        {
            Matrix Tr = new Matrix(t.col, t.row);
            for (int i = 0; i < t.row; i++)
                for (int j = 0; j < t.col; j++)
                {
                    Tr.M[j, i] = t.M[i, j];
                }
            return Tr;
        }
        public int Det()
        {
            int det = 0;
            switch (this.row)
            {
                case 1: return this.M[0, 0];
                case 2: return this.M[0, 0] * M[1, 1] - M[0, 1] * M[1, 0];
            }
            for (int n = 0; n < this.row; n++)
            {
                Matrix MR = this.Minor(0, n);
                det += ((n + 1) % 2 * 2 - 1) * this.M[0, n] * MR.Det();
            }

            return det;
        }

        public static Matrix operator +(Matrix S1, Matrix S2)
        {
            if (S1.col != S2.col || S1.row != S2.row)
            {
                throw new ArgumentException("Matrix don`t match");
            }
            for (int i = 0; i < S1.row; i++)
                for (int j = 0; j < S1.col; j++)
                    S1.M[i, j] = (S2.M[i, j] + S1.M[i, j]) > 0 ? 1 : 0;
            return S1;
        }

        public static Matrix operator -(Matrix S1, Matrix S2)
        {
            if (S1.col != S2.col || S1.row != S2.row)
            {
                throw new ArgumentException("Matrix don`t match");
            }
            for (int i = 0; i < S1.row; i++)
                for (int j = 0; j < S1.col; j++)
                    S1.M[i, j] -= S2.M[i, j];
            return S1;
        }

        public static Matrix operator *(Matrix S1, int k)
        {
            for (int i = 0; i < S1.row; i++)
                for (int j = 0; j < S1.col; j++)
                    S1.M[i, j] *= k;
            return S1;
        }

        public static Matrix operator *(Matrix S1, Matrix S2)
        {
            if (S1.col != S2.row)
            {
                throw new ArgumentException("Matrix don`t match");
            }
            Matrix tmp = new Matrix(S1.row, S2.col);
            for (int i = 0; i < S1.row; i++)
                for (int j = 0; j < S2.col; j++)
                {
                    tmp.M[i, j] = 0;
                    for (int z = 0; z < S1.col; z++)
                        tmp.M[i, j] = (S1.M[i, z] * S2.M[z, j] + tmp.M[i, j]) > 0 ? 1 : 0;
                }
            return tmp;
        }

        public static Matrix operator !(Matrix x)
        {
            Matrix inv = new Matrix(x.row, x.col);
            for (int i = 0; i < x.row; i++)
                for (int j = 0; j < x.col; j++)
                    inv.M[i, j] = x.M[i, j] == 0 ? 1 : 0;
            return inv;
        }

        public void Rand()
        {
            Random r = new Random();
            Func<int> rand = () => {

                return r.Next();
            };
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    this.M[i, j] = rand() % 21 + 1;
        }
        override public string ToString()
        {
            string s = "";
            for (int i = 0; i < row; i++)
            {
                s += "{ ";
                for (int j = 0; j < col; j++)
                    s += M[i, j].ToString() + " ";
                s += (i + 1 == row)?"}":"}\n";
            }
            return s;
        }
    }
}
