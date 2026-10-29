#!/bin/bash
# System report script - Berkinbay Abdigappar (ID 37409)
# Usage: ./scripts/Berkinbay_Abdigappar_system_report.sh [log_file]
# Without an argument the report is appended to logs/Berkinbay_Abdigappar_system_report.log

STUDENT_NAME="Berkinbay Abdigappar"
STUDENT_ID="37409"

# Project root = the folder above scripts/, so the script works from any directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_FILE="${1:-$PROJECT_DIR/logs/Berkinbay_Abdigappar_system_report.log}"

mkdir -p "$(dirname "$LOG_FILE")"

section() {
    echo
    echo "---------- $1 ----------"
}

{
    echo "=================================================="
    echo " SYSTEM REPORT"
    echo "=================================================="
    echo "Student:   $STUDENT_NAME"
    echo "ID:        $STUDENT_ID"
    echo "Date/time: $(date '+%Y-%m-%d %H:%M:%S %Z')"
    echo "Username:  $(whoami)"
    echo "Hostname:  $(hostname)"

    section "Operating system and kernel"
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        echo "OS:      ${PRETTY_NAME:-$NAME}"
    else
        echo "OS:      $(uname -s)"
    fi
    echo "Kernel:  $(uname -r)"
    echo "Arch:    $(uname -m)"

    section "CPU"
    if command -v lscpu > /dev/null 2>&1; then
        lscpu | grep -E '^(Model name|CPU\(s\)|Thread\(s\) per core|Architecture)'
    elif [ -f /proc/cpuinfo ]; then
        grep -m1 'model name' /proc/cpuinfo
        echo "CPU cores: $(grep -c '^processor' /proc/cpuinfo)"
    else
        sysctl -n machdep.cpu.brand_string 2>/dev/null || echo "CPU information not available"
    fi

    section "Memory"
    if command -v free > /dev/null 2>&1; then
        free -h
    else
        vm_stat 2>/dev/null || echo "Memory information not available"
    fi

    section "Disk usage"
    df -hP

    section "Available disk space (project folder)"
    df -hP "$PROJECT_DIR" | awk 'NR==2 {print "Available: " $4 " of " $2 " (" $5 " used) on " $6}'

    echo
    echo "Report created by $STUDENT_NAME"
    echo "=================================================="
} | tee -a "$LOG_FILE"

echo
echo "Saved to: $LOG_FILE"
