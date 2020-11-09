#$ -S /bin/sh
#$ -l h_vmem=6G
#$ -l h_rt=01:00:00
#$ -cwd
#$ -o /nfs-tmp/home/hpcsci/carloseg/PhD/SBST/svm/libsvm_@1/test.result
#$ -j y
#$ -M ceguimaraes@gmail.com
#$ -m a

module load python/2.7.5
module load gnuplot/4.4.2
python ~/PhD/SBST/svm/libsvm/tools/grid.py -v 10 ~/PhD/SBST/svm/libsvm_@1/train
module unload gnuplot/4.4.2
module unload python/2.7.5
