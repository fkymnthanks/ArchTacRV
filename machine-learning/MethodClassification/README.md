提交代码时请注意：

1. 请勿直接修改 master 分支，在自己的分支上先 pull master 分支，然后执行 git rebase master 后再修改。
2. 若要将所做修改合并到 master 分支，需要在右侧导航栏中选择 Merge Request，等待 maintainer 审核通过后合并。
3. 避免不必要的 commit，若要增量式修改代码且本次修改实际上不需要额外的 commit message，则可以使用 git commit --amend 来合并到上一次 commit 中。